/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author kireh
 */
public class User
{
    int uid;
    int rank;
    String name;
    
    public User(int uid, int rank, String name)
    {
        setUid(uid);
        setRank(rank);
        setName(name);
    }

    public int getUid()
    {
        return uid;
    }

    private void setUid(int uid)
    {
        this.uid = uid;
    }

    public int getRank()
    {
        return rank;
    }

    private void setRank(int rank)
    {
        this.rank = rank;
    }

    public String getName()
    {
        return name;
    }

    private void setName(String name)
    {
        this.name = name;
    }
    
    
}
