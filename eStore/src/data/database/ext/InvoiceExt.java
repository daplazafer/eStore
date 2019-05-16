
package data.database.ext;

import data.database.DatabaseModel.Invoice;
import java.util.Date;
import methods.Util;

/**
 *
 * @author Daniel Plaza
 */
public class InvoiceExt extends Invoice{
    
    private String date_string,amountTotal_string,amountTax_string,amountUntaxed_string;
    
    public InvoiceExt(Invoice invoice){
        super(invoice.getId(), invoice.getName(), invoice.getDate(), invoice.getAmountTotal(), invoice.getAmountTax(), invoice.getAmountUntaxed());
    }

    public InvoiceExt(Date date, double amountTotal, double amountTax, double amountUntaxed) {
        this.setDate(date);
        this.setAmountTotal(amountTotal);
        this.setAmountTax(amountTax);
        this.setAmountUntaxed(amountUntaxed);
    }    

    public String getAmountTotal_string() {
        return Util.format2d(this.getAmountTotal());
    }

    public String getAmountTax_string() {
        return Util.format2d(this.getAmountTax());
    }

    public String getAmountUntaxed_string() {
        return Util.format2d(this.getAmountUntaxed());
    }
    
    public String getDate_string() {
        return Util.SDF_DATEPICKER.format(this.getDate());
    } 
    
}
