/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */
import java.io.*;

public class Main {

	public static void main(String args[]) throws Exception{

		File file = new File("first-level.txt");
		//createFile(file);
		//readTxtFile(file);
		//	        writeTxtFile("我是写入的内容11",file);
		fileChaseFW("first-level.txt","66666666");


		IntOverflow.factorial();

		IntDivided0.intDivided0();

		FloatOverflow.multiply10();

		FloatINF.generateINF();
		FloatINF.divideINF();
		FloatINF.expINF();
		FloatINF.sinINF();
		FloatINF.propagateINF();
		FloatINF.interactINF();

		NaN.generateNaN();
		NaN.propagateNaN();
		NaN.interactNaN();

		Signed0.check0();
		Signed0.log0();
		Signed0.sinx_x();

		Gradual.generateXY();
		Gradual.XsubY();
		Gradual.XdividedY();
		Gradual.sinx_x();

		System.out.println("\n" + Pi.pi());
	}



	//	public static boolean createFile(File fileName)throws Exception{
	//        try{
	//            if(!fileName.exists()){
	//                fileName.createNewFile();
	//            }
	//        }catch(Exception e){
	//            e.printStackTrace();
	//        }
	//        return true;
	//    }
	//	
	//	
	//	public static String readTxtFile(File file){
	//        String result = "";
	//        try {
	//            InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"gbk");
	//            BufferedReader br = new BufferedReader(reader);
	//            String s = null;
	//            while((s=br.readLine())!=null){
	//                result = result  + s;
	//                System.out.println(s);
	//            }
	//        } catch (Exception e) {
	//            e.printStackTrace();
	//        }
	//        return result;
	//    }
	//	
	//	
	//	public static boolean writeTxtFile(String content,File fileName)throws Exception{
	//        RandomAccessFile mm=null;
	//        boolean flag=false;
	//        FileOutputStream fileOutputStream=null;
	//        try {
	//            fileOutputStream = new FileOutputStream(fileName);
	//            fileOutputStream.write(content.getBytes("gbk"));
	//            fileOutputStream.close();
	//            flag=true;
	//        } catch (Exception e) {
	//            e.printStackTrace();
	//        }
	//        return flag;
	//    }
	//	
	//	
	//	
	public static void fileChaseFW(String filePath, String content) {
		try {
			//构造函数中的第二个参数true表示以追加形式写文件
			FileWriter fw = new FileWriter(filePath,true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			System.out.println("文件写入失败！" + e);
		}
	}



}
