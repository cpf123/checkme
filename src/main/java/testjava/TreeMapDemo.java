package testjava;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Author:  heatdeath
 * Date:    2018/5/13
 * Desc:
 */
public class TreeMapDemo {
    public static void main(String[] args) {
        // creating maps
        TreeMap<Integer, String> treemap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        SortedMap<Integer, String> treemapincl;

        // populating tree map
        treemap.put(2, "two");
        treemap.put(1, "one");
        treemap.put(3, "three");
        treemap.put(6, "six");
        treemap.put(5, "five");

        System.out.println("Getting tail map");
        treemapincl = treemap.tailMap(3);
        System.out.println("Tail map values: " + treemapincl);

        treemapincl = treemap.headMap(3);
        System.out.println("Head map values: " + treemapincl);

        System.out.println("First key is: " + treemap.firstKey());
        System.out.println(treemap.lastKey());
    }
}
