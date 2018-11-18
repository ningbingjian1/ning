import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MClient {

    public static void main(String[] args) throws Exception {
        String[] serverlist = { "taibiao.mhqqw3.cfg.cnn1.cache.amazonaws.com.cn:11211"};
        SockIOPool pool = SockIOPool.getInstance();
  	    pool.setServers(serverlist);
      	pool.initialize();
        MemCachedClient mc = new MemCachedClient();
        getAllKeys(mc);
    }

    /**
     * 获取服务器上面所有的key
     * @return
     * @author liu787427876@126.com
     * @date 2013-12-4
     */
    public static List<String> getAllKeys(MemCachedClient memCachedClient) {
        List<String> list = new ArrayList<String>();
        Map<String, Map<String, String>> items = memCachedClient.statsItems();
        for (Iterator<String> itemIt = items.keySet().iterator(); itemIt.hasNext();) {
            String itemKey = itemIt.next();
            Map<String, String> maps = items.get(itemKey);
            for (Iterator<String> mapsIt = maps.keySet().iterator(); mapsIt.hasNext();) {
                String mapsKey = mapsIt.next();
                String mapsValue = maps.get(mapsKey);
                if (mapsKey.endsWith("number")) {  //memcached key 类型  item_str:integer:number_str
                    String[] arr = mapsKey.split(":");
                    int slabNumber = Integer.valueOf(arr[1].trim());
                    int limit = Integer.valueOf(mapsValue.trim());
                    Map<String, Map<String, String>> dumpMaps = memCachedClient.statsCacheDump(slabNumber, limit);
                    for (Iterator<String> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
                        String dumpKey = dumpIt.next();
                        Map<String, String> allMap = dumpMaps.get(dumpKey);
                        for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
                            String allKey = allIt.next();
                            System.out.println(allKey);
                            //list.add(allKey.trim());

                        }
                    }
                }
            }
        }
        return list;
    }
}