package me.kpotatto.survie.utils.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.kpotatto.survie.Survie;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class SavableObject<V extends SavableObject<V>> {

    private final String fileName;

    public SavableObject(String fileName){
        this.fileName = fileName;
    }

    public boolean exists(){
        return Files.exists(Paths.get(Survie.getInstance().getDataFolder().toString() + File.separatorChar + fileName + ".json"));
    }

    public void save(){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Path path = Paths.get(Survie.getInstance().getDataFolder().toString() + File.separatorChar + fileName + ".json");
            if(!Files.exists(path)){
                Files.createDirectories(Paths.get(Survie.getInstance().getDataFolder().toString()));
                Files.createFile(path);
            }
            Writer writer = Files.newBufferedWriter(path);
            gson.toJson(this, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<V> read() {
        try {
            if(this.exists()) {
                Gson gson = new Gson();
                Reader reader = Files.newBufferedReader(Paths.get(Survie.getInstance().getDataFolder().toString() + File.separatorChar + fileName + ".json"));
                V saved = (V) gson.fromJson(reader, this.getClass());
                return Optional.ofNullable(saved);
            }else{
                ((V)this).save();
                return Optional.of((V) this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static <T extends SavableObject<T>> T load(T obj) {
        Optional<T> pl = obj.read();
        return pl.orElse(obj);
    }

}
