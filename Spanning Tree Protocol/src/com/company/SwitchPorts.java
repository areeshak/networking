package com.company;

public class SwitchPorts
{
    int switchId;
    int portNo;
    int portCost;
    String portRole;
    LinkedList<SwitchPorts> switchNeighbours = new LinkedList<SwitchPorts>();

    SwitchPorts(int s_id, int p_no, int p_cost, String p_role)
    {
        switchId = s_id;
        portNo = p_no;
        portCost = p_cost;
        portRole = p_role;
    }

    @Override
    public String toString()
    {
        String str = switchId + "-" + portNo;
        return str;
    }

}
