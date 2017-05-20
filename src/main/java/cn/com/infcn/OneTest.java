package cn.com.infcn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.com.infcn.util.DbUtil;
import cn.com.infcn.util.PropertiesUtil;

/**
 * 修改config.properties
 * @author Administrator
 *
 */
public class OneTest {
    public static void main(String[] args) {
        
        Map<String, Object> data = DbUtil.getData(PropertiesUtil.getString("KEY"));
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(PropertiesUtil.getString("RESULT_PATH") + "\\result.txt"));
            Set<Entry<String, Object>> entrySet = data.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                fw.write(entry.getKey()+";");
                fw.write(System.getProperty("line.separator"));
            }
            fw.write("###########################################################");
            fw.write(System.getProperty("line.separator"));

            for (Entry<String, Object> entry : entrySet) {
                fw.write(entry.getKey()+";");
                fw.write(System.getProperty("line.separator"));
                fw.write("\t" + entry.getValue());
                fw.write(System.getProperty("line.separator"));
            }
            System.out.println("处理完成,共"+entrySet.size()+"张表！");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
