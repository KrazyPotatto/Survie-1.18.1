package me.kpotatto.survie.runnable;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.TpaRequest;

import java.util.Map;
import java.util.stream.Collectors;

public class TeleportRunnable implements Runnable {

    @Override
    public void run() {
        Survie.getInstance().teleportations = Survie.getInstance().teleportations.entrySet().stream().filter(e -> !e.getValue().tick()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        /*for (Map.Entry<UUID, TeleportationRequest> e: Survie.getInstance().teleportations.entrySet())
            if(e.getValue().tick()) Survie.getInstance().teleportations.remove(e.getKey());*/
        //for (TpaRequest tpa : Survie.getInstance().tpaRequests) if(!tpa.tick()) Survie.getInstance().tpaRequests.remove(tpa);
        Survie.getInstance().tpaRequests = Survie.getInstance().tpaRequests.stream().filter(TpaRequest::tick).collect(Collectors.toList());
        //for (TpaRequest tpa : Survie.getInstance().tpaHereRequests) if(!tpa.tick()) Survie.getInstance().tpaHereRequests.remove(tpa);
        Survie.getInstance().tpaHereRequests = Survie.getInstance().tpaHereRequests.stream().filter(TpaRequest::tick).collect(Collectors.toList());
    }

}
