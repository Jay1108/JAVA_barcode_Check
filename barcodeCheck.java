import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.synnex.erp.lbg.exception.BaseServiceException;


public class barcodeCheck{

  /**
   * 確定參數是否為國際條碼
   * 傳入參數：String 國際條碼
   * 回傳參數：String 檢核訊息
   * 檢核原則：
   * A.UPC(12碼)：
   *   1.位數由左到右，奇數碼 (1.3.5.7.9.11)、偶數碼 (2.4.6.8.10)、檢核碼(第12碼)
   *   2.(奇數碼加總*3) + (偶數碼加總) = 比對碼
   *   3.10-比對碼個位數 = 檢核碼則為UPC條碼
   * 範例：185245896844
   *   a.((1+5+4+8+6+4)*3)+(8+2+5+9+8) = 116(比對碼)
   *   b.10-6=4 與檢核碼相同
   *   c.此序號為UPC條碼
   * 
   * B.EAN、APN/GTIN(13碼)：
   *   1.位數由左到右
   *   2.奇數碼 (1.3.5.7.9.11)、偶數碼 (2.4.6.8.10.12)、檢核碼(第13碼)
   *   3.(奇數碼加總) + (偶數碼加總*3) = 比對碼
   *   4.10-比對碼個位數 = 檢核碼則為EAN或APN
   * 範例：9852475211136
   *   a.(9+5+4+5+1+1)+((8+2+7+2+1+3)*3) = 94(比對碼)
   *   b.10-4=6與檢核碼相同
   *   c.此序號為EAN或APN條碼
   */
  public static void main(String[] args){
    String warringMessage = "";
    Scanner scanner = new Scanner(System.in);
    System.out.println("請輸入檢核條碼(12或13碼)：");
    String barcode = scanner.next();
    try{
      if(StringUtils.isNotBlank(barcode)){  //判斷傳入參數是否為空值
        if(barcode.length() == 13 || barcode.length() == 12){//當傳入條碼長度為12或13才需檢核
          Pattern pattern = Pattern.compile("^[0-9]*$");
          //檢核是否有非法字元(不為數字)
          if(!pattern.matcher(barcode).matches()){
            warringMessage = "請輸入數字";
          }
          int length = barcode.length() % 2;//判斷傳入條碼的類型
          String checkCode = (String) barcode.substring(barcode.length() - 1);//取得條碼最後一碼，做為檢核碼
          int evenCode = 0;
          int oddCode = 0;
          int sum = 0;
          for(int i = 1; i <= 11 + length; i++){
            if(i % 2 == 1 - length)
              evenCode = evenCode + Character.getNumericValue(barcode.charAt(i - 1));//傳入條碼長度為13此總和為偶數和，反之為奇數和
            else
              oddCode = oddCode + Character.getNumericValue(barcode.charAt(i - 1));//傳入條碼長度為13此總和為奇數和，反之為偶數和
          }
          //將總和乘三取得比對碼
          evenCode = evenCode * 3;
          //"10 - 比對碼個位數"作為傳入條碼的檢核碼
          sum = 10 - ((oddCode + evenCode) % (10));
          if(sum == 10)
            sum = 0;
          //如傳輸條碼的檢核碼與條碼最後一碼的檢核碼相同，代表此傳入條碼為國際條碼
          if(StringUtils.equals(checkCode, String.valueOf(sum))){
            warringMessage = "Pass";
          }
        }else{
          warringMessage = "輸入條碼未滿12 or 13碼";
        }
      }else{
        warringMessage = "條碼未填入";
      }
    }catch(Exception e){
      warringMessage = "發生未知錯誤，請重新嘗試";
    }
    System.out.println(warringMessage);
  }
}
