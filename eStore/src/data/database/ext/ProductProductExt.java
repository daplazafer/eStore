/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.database.ext;

import data.database.DatabaseModel.ProductProduct;
import methods.Util;

/**
 *
 * @author Daniel Plaza
 */
public class ProductProductExt extends ProductProduct{
    
    private String categoryName,taxName;
    private String price_string,qty_string;
    private double taxAmount;
    
    public ProductProductExt(ProductProduct pp,String categoryName,String taxName,double taxAmount){
        super(pp.getId(),pp.getName(), pp.getDescription(), pp.isActive(), pp.getTaxId(), pp.getCategId(), pp.getQtyAvailable(), pp.getPriceUnit());
        this.categoryName = categoryName;
        this.taxName = taxName;
        this.price_string=Util.format2d(pp.getPriceUnit());
        this.qty_string=Util.format2d(pp.getQtyAvailable());
        this.taxAmount=taxAmount;
    }

    public ProductProductExt(int id, String name, String description, boolean active, int taxId, int categId, double qtyAvailable, double priceUnit,String categoryName, String taxName,double taxAmount) {
        super(id, name, description, active, taxId, categId, qtyAvailable, priceUnit);
        this.categoryName = categoryName;
        this.taxName = taxName;
        this.taxAmount=taxAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    
    
    public String getPrice_string() {
        return price_string;
    }

    public void setPrice_string(String price_string) {
        this.price_string = price_string;
    }

    public String getQty_string() {
        return qty_string;
    }

    public void setQty_string(String qty_string) {
        this.qty_string = qty_string;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }
    
    
}
