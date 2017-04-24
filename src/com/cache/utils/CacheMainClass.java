package com.cache.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CacheMainClass {
	public static int cacheNumber = 0;
	public static int cacheLineNumber = 0;
	public static char cacheMode = '\0';
	public static LinkedHashMap<Integer,CacheStatus> cache1 = new LinkedHashMap<Integer,CacheStatus>();
	public static LinkedHashMap<Integer,CacheStatus> cache2 = new LinkedHashMap<Integer,CacheStatus>();
	public static LinkedHashMap<Integer,CacheStatus> cache3 = new LinkedHashMap<Integer,CacheStatus>();
	public static boolean cache1HitFlag = false;
	public static boolean cache2HitFlag = false;
	public static boolean cache3HitFlag = false;
	public static ArrayList<LinkedHashMap<Integer,CacheStatus>> globalCacheContainer= new ArrayList<LinkedHashMap<Integer,CacheStatus>>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try{
        	initialiseCache();
        	while(true){
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        System.out.print("Enter input: ");
		        String input = br.readLine();
		        cacheNumber = Integer.parseInt(String.valueOf(input.charAt(0)));
		        if(cacheNumber >= 3){
		        	System.out.println("Please enter valid cache numbers from 0-2");
		        	System.exit(0);
		        }
		        cacheMode = input.charAt(1);
		        cacheLineNumber = Integer.parseInt(String.valueOf(input.charAt(2)));
		        if(cacheMode == 'r'){
		        	System.out.println("Reading at line "+cacheLineNumber+" in cache "+cacheNumber);
		        	getCacheStatus(cacheLineNumber);
		        	if(cacheNumber == 0){
		        		getCurrentCacheReadMode(cache1,cacheNumber,cacheLineNumber);
		        	}
		        	if(cacheNumber == 1){
		        		getCurrentCacheReadMode(cache2,cacheNumber,cacheLineNumber);
		        	}
		        	if(cacheNumber == 2){
		        		getCurrentCacheReadMode(cache3,cacheNumber,cacheLineNumber);
		        	}
		        }
		        else{
		        	System.out.println("Writing on line "+cacheLineNumber+" in cache "+cacheNumber);
		        	getCacheStatus(cacheLineNumber);
		        	if(cacheNumber == 0){
		        		getCurrentCacheWriteMode(cache1,cacheNumber,cacheLineNumber);
		        	}
		        	if(cacheNumber == 1){
		        		getCurrentCacheWriteMode(cache2,cacheNumber,cacheLineNumber);
		        	}
		        	if(cacheNumber == 2){
		        		getCurrentCacheWriteMode(cache3,cacheNumber,cacheLineNumber);
		        	}
		        }
        	}
        }catch(Exception e){
            e.printStackTrace();
        }
	}

	private static void getCurrentCacheWriteMode(
			LinkedHashMap<Integer, CacheStatus> getCache, int cacheNumber,
			int cacheLineNumber) {
		// TODO Auto-generated method stub
		if(cacheNumber == 0){
			probeOtherCachesWriteMode(cache2, cache3,cacheNumber, cacheLineNumber);
			if(cache1HitFlag){
				if(!getCache.get(cacheLineNumber).name().equalsIgnoreCase("modified")){
					System.out.println("Cache 0 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
					getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
				}
			}
			else{
				System.out.println("Cache 0 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
				getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
			}
		}
		else if(cacheNumber == 1){
			probeOtherCachesWriteMode(cache1, cache3,cacheNumber, cacheLineNumber);
			if(cache2HitFlag){
				if(getCache.get(cacheLineNumber).name().equalsIgnoreCase("modified")){
					System.out.println("Cache 1 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
					getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
				}
			}
			else{
				System.out.println("Cache 1 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
				getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
			}
		}
		else{
			probeOtherCachesWriteMode(cache1, cache2,cacheNumber, cacheLineNumber);
			if(cache3HitFlag){
				if(getCache.get(cacheLineNumber).name().equalsIgnoreCase("modified")){
					System.out.println("Cache 2 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
					getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
				}
			}
			else{
				System.out.println("Cache 2 state from "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.MODIFIED);
				getCache.put(cacheLineNumber, CacheStatus.MODIFIED);
			}
		}
	}

	private static void probeOtherCachesWriteMode(LinkedHashMap<Integer, CacheStatus> cache12,LinkedHashMap<Integer, CacheStatus> cache22, int cacheNumber,	int cacheLineNumber) {
		// TODO Auto-generated method stub
		CacheStatus statusA = cache12.get(cacheLineNumber);
		CacheStatus statusB = cache22.get(cacheLineNumber);
		if(statusA != CacheStatus.INVALID){
            if(statusA == CacheStatus.MODIFIED){
            	 System.out.println("Dirty Hit Encountered");
            	 System.out.println("Cache updated for line: "+cacheLineNumber+" "+cache12.get(cacheLineNumber).name()+" -> "+CacheStatus.INVALID);
            	 cache12.put(cacheLineNumber,CacheStatus.INVALID);
            }
            else{
	            if(statusA != CacheStatus.INVALID){
	            	System.out.println("Cache state updated from "+cache12.get(cacheLineNumber).name()+" -> "+CacheStatus.INVALID);
	            	cache12.put(cacheLineNumber,CacheStatus.INVALID);
	            }
            }
		}
		if(statusB != CacheStatus.INVALID){
            if(statusB == CacheStatus.MODIFIED){
            	 System.out.println("Dirty Hit  Encountered");
            	 System.out.println("Cache updated for line: "+cacheLineNumber+" "+cache22.get(cacheLineNumber).name()+" -> "+CacheStatus.INVALID);
            	 cache22.put(cacheLineNumber,CacheStatus.INVALID);
            }
            else{
	            if(statusB != CacheStatus.INVALID){
	            	System.out.println("Cache state from "+cache22.get(cacheLineNumber).name()+" -> "+CacheStatus.INVALID);
	            	cache22.put(cacheLineNumber,CacheStatus.INVALID);
	            }
            }
		}		
	}

	private static void getCacheStatus(int cacheLineNumber2) {
		if(!cache1.get(cacheLineNumber2).name().equalsIgnoreCase("invalid")){
			cache1HitFlag = true;
			System.out.println("Cache Hit for Cache 0");
		}
		else{
			System.out.println("Cache Miss for Cache 0");
			cache1HitFlag = false;
		}
		if(!cache2.get(cacheLineNumber2).name().equalsIgnoreCase("invalid")){
			cache2HitFlag = true;
			System.out.println("Cache Hit for Cache 1");
		}
		else{
			System.out.println("Cache Miss for Cache 1");
			cache2HitFlag = false;
		}
		if(!cache3.get(cacheLineNumber2).name().equalsIgnoreCase("invalid")){
			cache3HitFlag = true;
			System.out.println("Cache Hit for Cache 2");
		}
		else{
			System.out.println("Cache Miss for Cache 2");
			cache3HitFlag = false;
		}
	}

	private static void getCurrentCacheReadMode(LinkedHashMap<Integer, CacheStatus> getCache, int cacheNumber, int cacheLineNumber) {
		if(cacheNumber == 0){
			probeOtherCachesReadMode(cache2, cache3,cacheNumber, cacheLineNumber);
			if(cache1HitFlag){
				System.out.println("Cache 0 state for line: "+cacheLineNumber+" from "+getCache.get(cacheLineNumber).name()+" -> "+getCache.get(cacheLineNumber).name());
			}
			else{
				if(!cache2HitFlag && !cache3HitFlag){
					System.out.println("Cache 0  for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.EXCLUSIVE);
					getCache.put(cacheLineNumber, CacheStatus.EXCLUSIVE);
				}
				else{
					System.out.println("Cache 0  for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.SHARED);
					getCache.put(cacheLineNumber, CacheStatus.SHARED);
				}
			}
		}
		else if(cacheNumber == 1){
			probeOtherCachesReadMode(cache1, cache3,cacheNumber, cacheLineNumber);
			if(cache2HitFlag){
				System.out.println("Cache 1 state from "+getCache.get(cacheLineNumber).name()+" -> "+getCache.get(cacheLineNumber).name());
			}
			else{
				if(!cache1HitFlag && !cache3HitFlag){
					System.out.println("Cache 1  for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.EXCLUSIVE);
					getCache.put(cacheLineNumber, CacheStatus.EXCLUSIVE);
				}
				else{
					System.out.println("Cache 1 for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.SHARED);
					getCache.put(cacheLineNumber, CacheStatus.SHARED);
				}
			}
		}
		else{
			probeOtherCachesReadMode(cache1, cache2,cacheNumber, cacheLineNumber);
			if(cache3HitFlag){
				System.out.println("Cache 2 state from "+getCache.get(cacheLineNumber).name()+" -> "+getCache.get(cacheLineNumber).name());
			}
			else{
				if(!cache1HitFlag && !cache2HitFlag){
					System.out.println("Cache 2  for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.EXCLUSIVE);
					getCache.put(cacheLineNumber, CacheStatus.EXCLUSIVE);
				}
				else{
					System.out.println("Cache 2 for line: "+cacheLineNumber+" "+getCache.get(cacheLineNumber).name()+" -> "+CacheStatus.SHARED);
					getCache.put(cacheLineNumber, CacheStatus.SHARED);
				}
			}
		}
	}

	private static void probeOtherCachesReadMode(LinkedHashMap<Integer, CacheStatus> cacheA, LinkedHashMap<Integer, CacheStatus> cacheB, int cacheNumber, int cacheLineNumber) {
		CacheStatus statusA = cacheA.get(cacheLineNumber);
		CacheStatus statusB = cacheB.get(cacheLineNumber);
		if(statusA != CacheStatus.INVALID){
            if(statusA == CacheStatus.MODIFIED){
            	 System.out.println("Dirty Hit Encountered");
            	 System.out.println("Cache updated for line: "+cacheLineNumber+" "+cacheA.get(cacheLineNumber).name()+" -> "+CacheStatus.OWNER);
            	 cacheA.put(cacheLineNumber,CacheStatus.OWNER);
            }
            else{
	            if(statusA != CacheStatus.INVALID && statusA != CacheStatus.MODIFIED){
	            	System.out.println("Cache updated for line: "+cacheLineNumber+" "+cacheA.get(cacheLineNumber).name()+" -> "+CacheStatus.SHARED);
	            	 cacheA.put(cacheLineNumber,CacheStatus.SHARED);
	            }
            }
		}
		if(statusB != CacheStatus.INVALID){
            if(statusB == CacheStatus.MODIFIED){
            	 System.out.println("Dirty Hit Encountered");
            	 System.out.println("Cache updated for line: "+cacheLineNumber+" "+cacheB.get(cacheLineNumber).name()+" -> "+CacheStatus.OWNER);
            	 cacheB.put(cacheLineNumber,CacheStatus.OWNER);
            }
            else{
	            if(statusB != CacheStatus.INVALID && statusB != CacheStatus.MODIFIED){
	            	System.out.println("Cache updated for line: "+cacheLineNumber+" "+cacheB.get(cacheLineNumber).name()+" -> "+CacheStatus.SHARED);
	            	cacheB.put(cacheLineNumber,CacheStatus.SHARED);
	            }
            }
		}
	}
	private static void initialiseCache() {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++){
			cache1.put(i, CacheStatus.INVALID);
			cache2.put(i, CacheStatus.INVALID);
			cache3.put(i, CacheStatus.INVALID);
		}
		globalCacheContainer.add(cache1);
		globalCacheContainer.add(cache2);
		globalCacheContainer.add(cache3);
	}
}
