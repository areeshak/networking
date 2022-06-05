package com.company;

public class Switch
{
    int switchId;
    int noOfPorts;
    int rootId;
    int pathCost;
    int transBridgeId;
    String switchStatus;


    Switch(int s_id, int n_ports, int root, int cost, int tx, String s_status)
    {
        switchId = s_id;
        noOfPorts = n_ports;
        rootId = root;
        pathCost = cost;
        transBridgeId = tx;
        switchStatus = s_status;
    }

}
