/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.Map;

/**
 *
 * @author kireh
 */
public class Statistics 
{
    int cid;
    Map<String, Integer> frequencies;
    
    public Statistics(int id)
    {
        
    }

    public void setCid(int cid) 
    {
        this.cid = cid;
    }

    public int getCid() 
    {
        return cid;
    }
    
    
}
