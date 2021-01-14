import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.synnex.erp.lbg.exception.BaseServiceException;


public class barcodeCheck{
  
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
