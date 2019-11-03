package br.com.insertkoin.toaq;

public class Aluno {
    private String nome;
    private String ra;
    private String email;
    private String telefone;
    private String imei;

    public Aluno(){
        //this constructor is required
    }

    public Aluno(String nome,
            String ra,
            String email,
            String telefone,
            String imei) {
        this.nome = nome;
        this.ra = ra;
        this.email = email;
        this.telefone = telefone;
        this.imei = imei;
    }

    public String getNome() {
        return nome;
    }

    public String getRa() {
        return ra;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getImei() {
        return imei;
    }
}
