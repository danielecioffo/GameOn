package it.unipi.dii.inginf.dsmt.gameon.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void sortHashMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();

        // If the hashmap is empty, the returned hashmap is empty
        assertEquals(hashMap, Utils.sortHashMap(new HashMap<>(), 0));
        // If the argument is null, the returned value is null
        assertNull(Utils.sortHashMap(null, 0));

        hashMap.put("FirstItem", 1);
        hashMap.put("SecondItem", 3);
        hashMap.put("ThirdItem", 2);
        hashMap = Utils.sortHashMap(hashMap, 3);

        Iterator<Map.Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
        // Check the ordering
        assertEquals("SecondItem", iterator.next().getKey());
        assertEquals("ThirdItem", iterator.next().getKey());
        assertEquals("FirstItem", iterator.next().getKey());
    }
}