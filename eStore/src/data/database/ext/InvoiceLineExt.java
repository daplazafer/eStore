
package data.database.ext;

import data.database.DatabaseModel.InvoiceLine;
import methods.Util;

/**
 *
 * @author Daniel Plaza
 */
public class InvoiceLineExt extends InvoiceLine{
    private String productName,taxName,categName;
    private String qty_string,priceUnit_string,amountSubtotal_string;
    private double taxAmount;
    
    public InvoiceLineExt(InvoiceLine il,String productName,String taxName,String categName){
        this.setId(il.getId());
        this.setInvoiceId(il.getInvoiceId());
        this.setPriceUnit(il.getPriceUnit());
        this.setProductId(il.getProductId());
        this.setTaxId(il.getTaxId());
        this.setProductQty(il.getProductQty());
        this.productName = productName;
        this.taxName = taxName;
        this.categName = categName;
    }

    public InvoiceLineExt(int productId, double productQty, double priceUnit, int taxId,String productName, String taxName, String categName, double taxAmount) {
        this.setPriceUnit(priceUnit);
        this.setProductId(productId);
        this.setTaxId(taxId);
        this.setProductQty(productQty);
        this.productName = productName;
        this.taxName = taxName;
        this.categName = categName;
        this.taxAmount=taxAmount;

    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public String getQty_string() {
        return Util.format2d(this.getProductQty());
    }

    public String getPriceUnit_string() {
        return Util.format2d(this.getPriceUnit());
    }

    public String getAmountSubtotal_string() {
        return Util.format2d(this.getPriceUnit()*this.getProductQty());
    }
    
    

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    } 
    
}
