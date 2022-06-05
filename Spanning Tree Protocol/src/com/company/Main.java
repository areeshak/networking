package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here

        STP network = new STP(4, 8);
        network.addSwitch(2, 2);
        network.addSwitch(4,2);
        network.addSwitch(1, 2);
        network.addSwitch(3, 2);

        network.addPort(2,1,0);
        network.addPort(2,2,0);
        network.addPort(3,1,0);
        network.addPort(1,1,0);
        network.addPort(4,1,0);
        network.addPort(1,2,0);
        network.addPort(3,2,0);
        network.addPort(4,2,0);


        network.addConnection(1,1,2,1);
        network.addConnection(1,2,3,1);
        network.addConnection(2,2,4,1);
        network.addConnection(4,2,3,2);

        network.helloBPDU();

        System.out.println(network.switchStatus());
        System.out.println(network.portRolePrint());

    }
}
