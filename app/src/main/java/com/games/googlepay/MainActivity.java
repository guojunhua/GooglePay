package com.games.googlepay;

import java.util.ArrayList;

import org.xutils.common.Callback.CancelledException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.GooglePay;
import com.anjlab.android.iab.v3.ProductListBean;
import com.anjlab.android.iab.v3.TransactionDetails;

public class MainActivity extends Activity {

	static final String TAG = "TrivialDrive";
	private TextView tv;
//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.text);
		GooglePay.init(this, new GooglePay.IHPayHandler() {

			@Override
			public void onPurchaseHistoryRestored() {
				Log.d(TAG, "支付恢复成功！");
				Toast.makeText(MainActivity.this, "支付恢复成功", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onProductPurchased(String productId,
					TransactionDetails details) {
				Log.d(TAG, "支付成功：" + productId);
				Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT)
						.show();
				GooglePay.consumePurchase(productId);
				if (details != null && details.purchaseInfo != null) {
					GooglePay.sendServer("100", "10001", "88", "12345", details.purchaseInfo.responseData, details.purchaseInfo.signature,"asdf");
					tv.setText("");
					tv.setText(details.purchaseInfo.responseData+"\n"
							+ details.purchaseInfo.signature);
				}
			}

			@Override
			public void onBillingInitialized() {

			}

			@Override
			public void onBillingError(int errorCode, Throwable error) {
				Log.d(TAG, "Google支付失败：" + errorCode);
				Toast.makeText(MainActivity.this, "Google支付失败",
						Toast.LENGTH_SHORT).show();
			}

		});
		// 购买双倍金币（受管理商品）
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						GooglePay.buyPurchase(MainActivity.this, "test.google.game.1", "4");
//						TransactionDetails details = GooglePay.buyPurchase(MainActivity.this, "test.google.game.1", "4");
//						if (details != null && details.purchaseInfo != null) {
//							// 通知服务器
//							GooglePay.consumePurchase(details.productId);
//							String json = details.purchaseInfo.responseData;
//							String product = details.productId;
//						}
					}
				});
		// // 购买100猫币（不受管理商品）
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						GooglePay.productList("100000", "com.games.googlepay",
								"default", "1",
								new GooglePay.ProductListHandler() {

									@Override
									public void onSuccess(
											ArrayList<ProductListBean> data) {
									}

									@Override
									public void onFinished() {

									}

									@Override
									public void onError(Throwable ex,
											boolean isOnCallback) {

									}

									@Override
									public void onCancelled(
											CancelledException cex) {

									}
								});
					}
				});
		// // Restore Order
		findViewById(R.id.button3).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TransactionDetails oldDetail = GooglePay
								.getGoogleBilling();
						if (oldDetail != null && oldDetail.purchaseInfo != null) {
							tv.setText("");
							tv.setText(oldDetail.productId + "\n"
									+ oldDetail.purchaseToken + "\n"
									+ oldDetail.purchaseInfo + "\n"
									+ oldDetail.purchaseTime + "\n"
									+ oldDetail.orderId + "\n"
									+ oldDetail.purchaseInfo.responseData);
							GooglePay.consumePurchase(oldDetail.productId);
						}
					}
				});
	}

	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (GooglePay.onActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} 
	}

	// @Override
	protected void onDestroy() {

		super.onDestroy();
		GooglePay.onDestroy();
	}
}
