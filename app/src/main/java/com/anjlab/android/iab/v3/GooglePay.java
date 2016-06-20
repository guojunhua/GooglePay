package com.anjlab.android.iab.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.games.googlepay.MyApp;


/**
 * Created by 郭君华 on 2016/5/26. Email：guojunhua3369@163.com
 */
public class GooglePay {
	public static final String PRODUCTLIST = "http://c.gamehetu.com/%s/product?package=%s&os=android&channel=%s&server=%s";
	public static final String SENDSERVICE = "http://c.gamehetu.com/order/notice/google?app=%s&uid=%s&coo_server=%s&coo_uid=%s&extra=%s&receipt=%s&signature=%s";
	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;
	private static BillingProcessor googlePay = null;
	private static IHPayHandler payResultHandler = null;
	private static ProductListHandler productListHandler = null;
	public final static String GoogleMerchantId = null;
	public final static String GoogleRSAPublickey = null;
	private static Timer timer;

	public interface ProductListHandler {
		// 产品列表的回调�
		void onSuccess(ArrayList<ProductListBean> data);

		void onError(Throwable ex, boolean isOnCallback);

		void onCancelled(Callback.CancelledException cex);

		void onFinished();
	}

	public interface IHPayHandler {
		// google支付部分回调�
		void onProductPurchased(String productId, TransactionDetails details);

		void onPurchaseHistoryRestored();

		void onBillingError(int errorCode, Throwable error);

		void onBillingInitialized();
	}

	/**
	 *产品列表请求方法
	 * @param appId
	 *            应用id
	 * @param packageName
	 *            应用包名
	 * @param channel
	 *            应用的渠道号
	 * @param server
	 *            服务器
	 */
	public static void productList(String appId, String packageName,
			String channel, String server,
			final ProductListHandler produceListHandlers) {
		productListHandler = produceListHandlers;
		String url = String.format(PRODUCTLIST, appId, packageName, channel,
				server);
		RequestParams params = new RequestParams(url);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				ArrayList<ProductListBean> data = (ArrayList<ProductListBean>) parserJson(result);
				productListHandler.onSuccess(data);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				productListHandler.onError(ex, isOnCallback);
			}

			@Override
			public void onCancelled(Callback.CancelledException cex) {
				productListHandler.onCancelled(cex);
			}

			@Override
			public void onFinished() {
				productListHandler.onFinished();
			}
		});
	}

	/**
	 * 向服务器发送成功的充值订单
	 * @param appId 应用id
	 * @param uid 用户id
	 * @param coo_server 游戏区服
	 * @param coo_uid 游戏角色id
	 * @param receipt 充值成功结果
	 * @param signature 充值成功签名
	 */
	public static void sendServer(final String appId,
			final String uid, final String coo_server, final String coo_uid,
			final String receipt, final String signature,final String extra) {
		String url = String.format(SENDSERVICE, appId, uid, coo_server,
				coo_uid, extra,receipt, signature);
		RequestParams params = new RequestParams(url);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				saveData(appId, uid, coo_server, coo_uid, receipt, signature, 1,extra);
			}

			@Override
			public void onCancelled(Callback.CancelledException cex) {
				saveData(appId, uid, coo_server, coo_uid, receipt, signature, 1,extra);
			}

			@Override
			public void onFinished() {

			}
		});
	}
	
	/**
	 * 向服务器发送失败时的数据保存
	 * @param appId
	 * @param uid
	 * @param coo_server
	 * @param coo_uid
	 * @param receipt
	 * @param signature
	 * @param isSuccess 是否成功发送数据
	 */
	public static void saveData(String appId, String uid, String coo_server,
			String coo_uid, String receipt, String signature, int isSuccess,String extra) {
		DbManager db = x.getDb(MyApp.getInstance()
				.getDaoConfig());
		PayRecordBean payRecordBean = new PayRecordBean();
		payRecordBean.setAppId(appId);
		payRecordBean.setCoo_server(coo_server);
		payRecordBean.setCoo_uid(coo_uid);
		payRecordBean.setUid(uid);
		payRecordBean.setReceipt(receipt);
		payRecordBean.setSignature(signature);
		payRecordBean.setIsSuccess(isSuccess);
		payRecordBean.setExtra(extra);
		try {
			db.saveOrUpdate(payRecordBean);
			startSyncTableStauts(10);
		} catch (DbException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * google支付初始化
	 * @param activity
	 * @param handler
	 */
	public static void init(Activity activity, IHPayHandler handler) {
		payResultHandler = handler;
		googlePay = new BillingProcessor(activity, GoogleRSAPublickey,
				GoogleMerchantId, new BillingProcessor.IBillingHandler() {
					@Override
					public void onProductPurchased(String productId,
							TransactionDetails details) {
						payResultHandler.onProductPurchased(productId, details);
					}

					@Override
					public void onBillingError(int errorCode, Throwable error) {
						payResultHandler.onBillingError(errorCode, error);
					}

					@Override
					public void onBillingInitialized() {
						payResultHandler.onBillingInitialized();
					}

					@Override
					public void onPurchaseHistoryRestored() {
						payResultHandler.onPurchaseHistoryRestored();
					}
				});
	}
	
	/**
	 * 直接开始google支付
	 * @param activity
	 * @param productId
	 * @param purchasePayload
	 * @return
	 */
	public static TransactionDetails buyPurchase(Activity activity,
			String productId, String purchasePayload){
		if (!BillingProcessor.isIabServiceAvailable(activity)) {
			Toast.makeText(
					activity,
					"In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		if (googlePay.isPurchased(productId)) {
			TransactionDetails transDetails = googlePay
					.getPurchaseTransactionDetails(productId);
			if (transDetails != null)
				return transDetails;
		}

		googlePay.purchase(activity, productId, purchasePayload);
		return null;
	} 

	// 查询没有到账的google支付详情
	public static TransactionDetails getGoogleBilling() {
		for (String productId : googlePay.listOwnedProducts()) {
			TransactionDetails transDetails = googlePay
					.getPurchaseTransactionDetails(productId);
			if (transDetails != null)
				return transDetails;
		}

		return null;
	}

	// 道具发送完成，需要消费掉google产品
	public static void consumePurchase(String productId) {
		googlePay.consumePurchase(productId);
	}

	public static boolean onActivityResult(int requestCode, int resultCode,
			Intent data) {
		return googlePay.handleActivityResult(requestCode, resultCode, data);
	}

	public static void onDestroy() {
		if (googlePay != null) {
			googlePay.release();
			googlePay = null;
		}
	}

	public static List<ProductListBean> parserJson(String json) {
		List<ProductListBean> data = null;
		try {
			data = new ArrayList<>();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ProductListBean productListBean = new ProductListBean();
				productListBean.setId(jsonObject.getString("id"));
				productListBean.setName(jsonObject.getString("name"));
				productListBean.setPrice(jsonObject.getString("price"));
				productListBean.setCurrency(jsonObject.getString("currency"));
				productListBean.setProduct_id(jsonObject
						.getString("product_id"));
				productListBean.setMoney(jsonObject.getString("money"));
				productListBean.setSort(jsonObject.getString("sort"));
				productListBean.setRemark(jsonObject.getString("remark"));
				data.add(productListBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	public static class TableStatusSyncTimerTask extends TimerTask{

		@Override
		public void run() {
			try {
				DbManager db = x.getDb(MyApp.getInstance().getDaoConfig());
				List<PayRecordBean> payRecordBeans = db.selector(PayRecordBean.class).findAll();
				if (payRecordBeans.size()==0) {
					stopSyncTableStatus();
				}else{
					for (PayRecordBean payRecordBean : payRecordBeans) {
						if (payRecordBean.getIsSuccess()==1) {
							sendServer(payRecordBean.getAppId(), payRecordBean.getUid(), payRecordBean.getCoo_server(), payRecordBean.getCoo_uid(), payRecordBean.getReceipt(), payRecordBean.getSignature(),payRecordBean.getExtra());
						}else if (payRecordBean.getIsSuccess()==0) {
							DbManager db1 = x.getDb(MyApp.getInstance().getDaoConfig());
							db1.delete(payRecordBean);
						}
					}
				}
			} catch (DbException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	public static void startSyncTableStauts(int seconds) {
		TimerTask timerTask =new TableStatusSyncTimerTask();
		timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask,100, seconds * 1000);
	}
	public static void stopSyncTableStatus() {
		timer.cancel();
	}
}
