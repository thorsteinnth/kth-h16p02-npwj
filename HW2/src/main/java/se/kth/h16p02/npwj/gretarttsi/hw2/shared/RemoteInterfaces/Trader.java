package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import java.rmi.Remote;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;

public interface Trader extends Remote {

    void wishIsAvailable (Item item);

    void itemSold(Item item);

}
