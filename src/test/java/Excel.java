import static org.junit.Assert.*;

import java.awt.print.Book;
import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pmz.pojo.app_info;
import com.pmz.service.app_info.app_infoService;
import com.pmz.service.app_info.app_infoServiceImpl;


public class Excel {

	@Test
	public void test()throws Exception {
		WritableWorkbook book=Workbook.createWorkbook(new File("F:/pmz.xls"));
		WritableSheet sheet=book.createSheet("APPINFO表",0);
		// 表字段名
        sheet.addCell(new jxl.write.Label(0, 0, "软件名称"));
        sheet.addCell(new jxl.write.Label(1, 0, "APK名称"));
        sheet.addCell(new jxl.write.Label(2, 0, "软件大小(单位:M)"));
        sheet.addCell(new jxl.write.Label(3, 0, "所属平台"));
        sheet.addCell(new jxl.write.Label(4, 0, "所属分类(一级分类、二级分类、三级分类)"));
        sheet.addCell(new jxl.write.Label(5, 0, "状态"));
        sheet.addCell(new jxl.write.Label(6, 0, "下载次数"));
        sheet.addCell(new jxl.write.Label(7, 0, "最新版本号"));

        app_infoService ai=new app_infoServiceImpl();
        
        List<app_info> list=ai.getAllList(null, null, null, null, null, null, null, 1,9999);
        
        for (int i = 0; i <list.size(); i++) {
        	app_info app=list.get(i);
        	sheet.addCell(new jxl.write.Label(0,i+1,app.getSoftwareName()));
        	sheet.addCell(new jxl.write.Label(1,i+1,app.getAPKName()));
        	sheet.addCell(new jxl.write.Label(2,i+1,""+app.getSoftwareSize()));
        	sheet.addCell(new jxl.write.Label(3,i+1,app.getPingtai()));
        	sheet.addCell(new jxl.write.Label(4,i+1,app.getLei1()+">"+app.getLei2()+">"+app.getLei3()));
        	sheet.addCell(new jxl.write.Label(5,i+1,app.getZhuangtai()));
        	sheet.addCell(new jxl.write.Label(6,i+1,""+app.getDownloads()));
        	sheet.addCell(new jxl.write.Label(7,i+1,app.getVersionNo()));
		}
        
        book.write();
        book.close();
	}

}
