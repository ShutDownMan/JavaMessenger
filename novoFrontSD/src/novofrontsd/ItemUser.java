package novofrontsd;

import java.io.File;

/**
 *
 * @author laril
 */
public class ItemUser {
    
    private String nomeUser;
    private String caminhoIcon;
    
    public ItemUser(String nome){
        String caminho = new File("").getAbsolutePath();
        caminho += "/src/resources/iconUsers2.png";
        this.nomeUser = nome;
        this.caminhoIcon = caminho;
    }
    
    public String getNome(){
        return nomeUser;
    }
    
    public String getCaminho(){
        return caminhoIcon;
    }
    
    public void setNome(String nome){
        this.nomeUser = nome;
    }

    public String ToString(){
        return String.format("Nome: %s | caminho: %s", nomeUser, caminhoIcon);
    }
}