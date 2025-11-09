package edu.ptithcm.model;

import java.util.Date;

public class ProductModel {
    private final String product_id;
    private final String name;
    private final String category_id;
    private final String category;
    private final double cost_price;
    private final double sell_price;
    private final Date expiry_date;
    private final Boolean is_active;

    private ProductModel(Builder builder){
        this.product_id = builder.product_id;
        this.name = builder.name;
        this.category_id = builder.category_id;
        this.category = builder.category;
        this.cost_price = builder.cost_price;
        this.sell_price = builder.sell_price;
        this.expiry_date = builder.expiry_date;
        this.is_active = builder.is_active;
    }


    public static class Builder{
        private String product_id;
        private String name;
        private String category_id;
        private String category;
        private double cost_price;
        private double sell_price;
        private Date expiry_date;
        private Boolean is_active;

        public Builder product_id(String product_id){
            this.product_id = product_id;
            return this;
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        
        public Builder category_id(String category_id){
            this.category_id = category_id;
            return this;
        }
        
        public Builder category(String category){
            this.category = category;
            return this;
        }
        
        public Builder sell_price(double sell_price){
            this.sell_price = sell_price;
            return this;
        }
        
        public Builder cost_price(double cost_price){
            this.cost_price = cost_price;
            return this;
        }

        public Builder expiry_date(Date expiry_date){
            this.expiry_date = expiry_date;
            return this;
        }


        public Builder is_active(boolean is_active){
            this.is_active = is_active;
            return this;
        }

        public ProductModel build(){
            return new ProductModel(this);
        }

    }

}
