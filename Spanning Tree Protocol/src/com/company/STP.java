package com.company;

public class STP
{
    Switch[] switchList;
    SwitchPorts[] portList;
    int switchCount, portCount;

    STP(int noOfSwitch, int noOfPorts)
    {
        switchList = new Switch[noOfSwitch];                                                                            //array list of type switches to maintain
        portList = new SwitchPorts[noOfPorts];                                                                          //array list of type switch port created
        switchCount = 0;
        portCount = 0;
    }

    public void addSwitch(int s_id, int no_ports)
    {
        Switch s = new Switch(s_id, no_ports, s_id, 0, s_id, "root switch");                                //add a new switch in the topology and declares itself as root switch
        switchList[switchCount++] = s;                                                                                  //insert switch id details and number of ports in array
    }

    public void addPort(int s_id, int p_no, int p_cost)
    {
        SwitchPorts sp = new SwitchPorts(s_id, p_no, p_cost, null);                                               //insert port number, cost and role in array (no port role assigned initially)
        portList[portCount++] = sp;
    }

    public void addConnection(int switch1, int port1, int switch2, int port2)
    {
        int i,j;
        for(i = 0;i < portCount; i++)
        {
            if(portList[i] != null && switch1 == portList[i].switchId && port1 == portList[i].portNo)
            {
                break;
            }
        }

        for(j = 0;j < portCount; j++)
        {
            if(portList[j] != null && switch2 == portList[j].switchId && port2 == portList[j].portNo)
            {
                break;
            }
        }
        portList[i].switchNeighbours.insert(portList[j]);
        portList[j].switchNeighbours.insert(portList[i]);
    }

    public void helloBPDU()
    {
        int currentSwitch;
        int c_portNo = 0, count = 0;
        int n_switch, n_port, cost;
        String str = "";
        for(int i = 0; i < switchCount; i++)
        {
            currentSwitch = switchList[i].switchId;
            cost = 19;
            c_portNo = 1;
            while(c_portNo <= switchList[i].noOfPorts)                                                                  //find all switch ports of current switch to send hello bpdu
            {
               str = "";
               for(int k = 0; k < portCount; k++)
               {
                   if(portList[k].switchId == currentSwitch && portList[k].portNo == c_portNo)                          //find port numbers of current switch
                   {
                       str = portList[k].switchNeighbours.toString();
                       break;
                   }
               }

               n_switch =  Integer.parseInt(String.valueOf(str.charAt(0)));                                             //neighbour switch of current switch port
               n_port =  Integer.parseInt(String.valueOf(str.charAt(2)));                                               //port number of neighbour switch

               for(int j = 0; j < switchCount; j++)                                                                     //find switch that was in neighbour for current switch in switch list
               {
                   if(switchList[j].switchId == n_switch )                                                              //compare neighbour switch bpdu with current switch bpdu
                   {
                       if(switchList[j].rootId > switchList[i].rootId)                                                  //if current switch bpdu better then neighbour switch --> change root id in neighbour switch
                       {
                           switchList[j].rootId = switchList[i].rootId;                                                 //change root id
                           for(int k = 0; k < portCount; k++)
                           {
                               if(portList[k].switchId == switchList[j].switchId && portList[k].portNo == n_port)       //find neighbour switch port numbers in port list
                               {
                                   portList[k].portCost = switchList[i].pathCost + switchList[j].pathCost + cost;
                                   break;
                               }
                           }
                           switchList[j].switchStatus = "non root switch";
                       }
                       else if(switchList[j].rootId < switchList[i].rootId)                                             //if neighbour switch bpdu better then current switch --> change root id of current switch
                       {
                           switchList[i].rootId = switchList[j].rootId;                                                 //change root id
                           for(int l = 0; l < portCount; l++)
                           {
                               if(portList[l].switchId == switchList[i].switchId && portList[l].portNo == c_portNo)      //find neighbour switch port numbers in port list
                               {
                                   {
                                       portList[l].portCost = switchList[j].pathCost + cost;
                                       switchList[i].pathCost = portList[l].portCost;
                                   }
                                   break;
                               }
                           }
                           switchList[i].switchStatus = "non root switch";
                       }
                       else if(switchList[i].switchStatus != "root switch")                                                                                           //if root node and switch root node same
                       {
                           for(int l = 0; l < portCount; l++)
                           {
                               if(portList[l].switchId == switchList[i].switchId && portList[l].portNo == c_portNo)     //find neighbour switch port numbers in port list
                               {
                                   {
                                       portList[l].portCost = switchList[j].pathCost + cost;
                                       switchList[i].pathCost = portList[l].portCost;
                                   }
                                   break;
                               }
                           }
                       }
                       break;
                   }
               }
               c_portNo++;
               count++;
            }
        }
        for(int s = 0; s < switchCount; s++)                                                                             //ports connected to same switch with one path to root node
        {
            if(switchList[s].switchStatus != "root switch")
            for(int k = 0; k < portCount; k++)
            {
                if(portList[k].switchId == switchList[s].switchId && portList[k].portCost == 0)
                {
                    portList[k].portCost = switchList[s].pathCost;
                }
            }
        }
        portRoles();
    }

    private void portRoles()
    {
        int[] compareCost = new int[5];                                                                                 //to store cost of port number
        int[] no = new int[5];                                                                                          //to store position of port numbers in port list array

        int  currentSwitch = 0, noOfPorts = 0, minCost = 0, minPort = 0,n_port = 0, n_switch = 0, k = 0;
        String n = "";

        for(int i = 0; i < switchCount; i++)
        {
            k = 0;
            currentSwitch = switchList[i].switchId;
            noOfPorts = switchList[i].noOfPorts;                                
            if(switchList[i].switchStatus.equals("root switch"))                              //if switch root switch
            {
                for(int j = 0; j < portCount; j++)
                {
                    if(currentSwitch == portList[j].switchId)
                    {
                        portList[j].portRole = "DP";
                    }
                }
            }
            else                                                                        //non root switch
            {
                for (int j = 0; j < portCount; j++)
                {
                    if (currentSwitch == portList[j].switchId)
                    {
                        no[k] = j;
                        compareCost[k] = portList[j].portCost;
                        k++;
                    }
                }

                minCost = compareCost[0];
                minPort = no[0];
                
                for (int z = 1; z < k; z++)
                {
                    if (minCost > compareCost[z])
                    {
                        minCost = compareCost[z];
                        minPort = no[z];
                    }
                }

                portList[minPort].portRole = "RP";
                n = portList[minPort].switchNeighbours.toString();                                                      //find neighbour port of RP to mark Dp
                n_switch = Integer.parseInt(String.valueOf(n.charAt(0)));                                               //neighbour switch of current switch port
                n_port = Integer.parseInt(String.valueOf(n.charAt(2)));                                                 //port number of neighbour switch

                for (int l = 0; l < portCount; l++)
                {
                    if(portList[l].switchId == n_switch && portList[l].portNo == n_port)
                    {
                        if(portList[l].portRole == null)
                        {
                            portList[l].portRole = "DP";
                        }
                        break;
                    }
                }
            }
        }
        for(int b = 0; b < portCount; b++)
        {
            if(portList[b].portRole == null)
            {
                portList[b].portRole = "AP";
            }
        }
    }

    public String switchStatus()
    {
        String str = "";
        for(int i = 0; i < switchCount; i++)
        {
            str = str + "Switch Id: " + switchList[i].switchId + "    Status: " + switchList[i].switchStatus + " (Root Id: " + switchList[i].rootId + ")\n";
        }
        return str;
    }

    public String portRolePrint()
    {
        String str = "";
        for(int j = 0; j < portCount; j++)
        {
            str = str + "S" + portList[j].switchId + " P" + portList[j].portNo + " --> Cost: " + portList[j].portCost + "   Port Role: " + portList[j].portRole +  "\n";
        }
        return str;
    }

    @Override
    public String toString()
    {
        String str = "";
        for(int i = 0; i < portCount; i++)
        {
            if(portList[i] == null)
            {
                str = str + "[" + i + "] " + "null\n";
            }
            else
            {
                str = str + "Switch " + portList[i].switchId + " Port No=" +  portList[i].portNo + "-->" + portList[i].switchNeighbours.toString() + "\n";
            }
        }
        return str;
    }

}
