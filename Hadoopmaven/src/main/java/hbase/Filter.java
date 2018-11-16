package hbase;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by newforesee on 2018/9/12.
 */
public class Filter {
    public static void main(String[] args) {
        //1.创建一个过滤器链
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);

        //2.构过滤器比较字段值是否满足条件
        SingleColumnValueFilter scvf = new SingleColumnValueFilter(Bytes.toBytes("base_info")
                , Bytes.toBytes("age"),
                CompareFilter.CompareOp.LESS,
                Bytes.toBytes("60"));
        SingleColumnValueFilter scvf1 = new SingleColumnValueFilter(Bytes.toBytes("base_info")
                , Bytes.toBytes("name"),
                CompareFilter.CompareOp.GREATER,
                Bytes.toBytes("hyy"));
        filterList.addFilter(scvf);
        filterList.addFilter(scvf1);

        //3.构造扫描器
        Scan scan = new Scan();
        //4.将过滤器链关联到扫描器
        scan.setFilter(filterList);
        //5.获取表对象



    }

}
