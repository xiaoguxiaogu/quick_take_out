package com.me.test;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chen
 * @Date 2022-04-19-20:12
 */
public class Test1 {
    private List<List<Integer>> ans;
    private List<Integer> tmp;

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        ans=new ArrayList<>();
        tmp=new ArrayList<>();
        if(candidates.length==0){
            return ans;
        }
        backtracking(candidates,target,0,0);
        return  ans;
    }


    private void backtracking(int[] candidates,int target,int idx,int curSum){
        //终止条件
        if(curSum>=target){
            if(curSum==target) ans.add(new ArrayList<Integer>(tmp));
            return ;
        }
        if(idx==candidates.length){
            return ;
        }

        //本层循环
        for(int i=idx;i<candidates.length;i++){
            tmp.add(candidates[i]);
            curSum+=candidates[i];
            backtracking(candidates,target,idx+1,curSum);
            tmp.remove(tmp.size()-1);
            curSum-=candidates[i];
        }
    }
    @Test
    public void test1(){
        List<List<Integer>> lists = combinationSum(new int[]{2,3,6,7}, 7);
        for (List<Integer> list : lists) {
            for (Integer integer : list) {
                System.out.println(integer+" ");
            }
            System.out.println();
        }


    }
}
