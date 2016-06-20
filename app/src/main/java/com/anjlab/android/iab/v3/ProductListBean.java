package com.anjlab.android.iab.v3;

/**
 * Created by 郭君华 on 2016/5/26.
 * Email：guojunhua3369@163.com
 */
public class ProductListBean {

    /**
     * id : 103
     * name : 苹果test1
     * price : 0.99
     * currency : USD
     * product_id : com.sincity.topup.3
     * money : 0
     * sort : 0
     * remark :
     */

    private String id;
    private String name;
    private String price;
    private String currency;
    private String product_id;
    private String money;
    private String sort;
    private String remark;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getMoney() {
        return money;
    }

    public String getSort() {
        return sort;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "ProductListBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                ", product_id='" + product_id + '\'' +
                ", money='" + money + '\'' +
                ", sort='" + sort + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
