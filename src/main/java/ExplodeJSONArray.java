import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * initialize限定输入和输出的类型，具体怎么做在process中
 * Hive中每一种数据类型都有对应的object inspector
 * explode不仅能炸裂数组，也能炸裂Map等（多出来两列，一列K一列V）
 * 为什么要自定义：对Hive而言，JSON数组还只是个字符串而已，所以explode不起作用，只能自定义UDTF
 * initialize限定输入输出类型；ObjectInspector封装了Hive的数据类型
 * process处理逻辑，循环调用forward输出
 * SparkSQL（我们当前项目）本身不支持UDTF函数，但是可以用Hive的UDTF函数，所以有对Hive的依赖。但是这个依赖版本比较低，因此我们只能用这个过期的initialize，新的initializeSparkSQL不认
 * initialize没有处理任何数据
 * 如果修改了自定义函数重新生成jar包怎么处理？只需要替换HDFS路径上的旧jar包，然后重启Hive客户端即可
 */

public class ExplodeJSONArray extends GenericUDTF {
    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {

        // 1 参数合法性检查
        if (argOIs.length != 1) {
            throw new UDFArgumentException("ExplodeJSONArray 只需要一个参数");
        }

        // 2 第一个参数必须为string
        if (!"string".equals(argOIs[0].getTypeName())) {
            throw new UDFArgumentException("json_array_to_struct_array的第1个参数应为string类型");
        }

        // 3 定义返回值名称和类型
        List<String> fieldNames = new ArrayList<String>();
        List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        fieldNames.add("items");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    @Override
    public void process(Object[] objects) throws HiveException {
        // 1 获取传入的数据
        String jsonArray = objects[0].toString();

        // 2 将string转换为json数组
        JSONArray actions = new JSONArray(jsonArray);

        // 3 循环一次，取出数组中的一个json，并写出
        for (int i = 0; i < actions.length(); i++) {

            String[] result = new String[1];
            result[0] = actions.getString(i);
            forward(result);
        }
    }

    @Override
    public void close() throws HiveException {

    }
}
