import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// Classe que representa uma aeronave, gerencia assentos, passageiros e cálculos de faturamento
public class Aeronave {
    private Scanner scanner = new Scanner(System.in); // Scanner para entrada do usuário
    private Passageiro[][] aeronave; // Matriz que representa os assentos da aeronave
    private ArrayList<Passageiro> passageiros; // Lista de passageiros do voo
    private LocalDate partida; // Data do voo
    int hoje; // Representa o dia atual em relação ao início do sistema
    private double faturamento; // Total arrecadado com as passagens
    private int passagens; // Contador de passagens emitidas
    private DateTimeFormatter formatter; // Formato de data no padrão "dd/MM/yyyy"

    // Construtor da classe
    public Aeronave(LocalDate partida, int hoje) {
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.aeronave = new Passageiro[11][3]; // Assentos distribuídos em 11 fileiras e 3 colunas
        this.partida = partida;
        this.hoje = hoje;
        this.passagens = 0;
        this.passageiros = new ArrayList<Passageiro>();
        this.faturamento = 0.00;
    }

    // Exibe os assentos disponíveis na aeronave
    public void assentosDisponiveis() {
        System.out.println("    A     B  C");
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 3; j++) {
                int cont = i + 1;
                if (aeronave[i][j] == null && j == 2) {
                    System.out.println("[ ]");
                } else if (aeronave[i][j] == null && j == 1) {
                    System.out.print("[ ]");
                } else if (aeronave[i][j] == null && j == 0) {
                    System.out.print(cont + "   [ ]   ");
                } else if (aeronave[i][j] != null && j == 2) {
                    System.out.println("[x]");
                } else if (aeronave[i][j] != null && j == 1) {
                    System.out.print("[x]");
                } else if (aeronave[i][j] != null && j == 0) {
                    System.out.print(cont + "   [x]   ");
                }
            }
        }
    }

    // Adiciona um novo passageiro ao voo
    public void novoPassageiro(Passageiro p) {
        this.assentosDisponiveis(); // Exibe os assentos disponíveis
        int fileira = 0, letra = 0;

        if (buscaPassageiro(p) == null) { // Verifica se o passageiro já está no voo
            do {
                System.out.print("Fileira: ");
                fileira = scanner.nextInt() - 1; // Subtrai 1 para alinhar com o índice do array
                scanner.nextLine();
                System.out.print("Letra: ");
                String aux = scanner.nextLine();
                letra = aux.toUpperCase().charAt(0) - 65; // Converte letra em índice (A=0, B=1, C=2)

                if (fileira > 10 || fileira < 0 || letra < 0 || letra > 2) {
                    System.out.println("Assento inválido, tente novamente!");
                }
            } while (fileira > 10 || fileira < 0 || letra < 0 || letra > 2);

            if (this.aeronave[fileira][letra] == null) { // Verifica se o assento está livre
                this.aeronave[fileira][letra] = p;
                this.passageiros.add(p); // Adiciona o passageiro à lista
                this.faturamento += calcPassagem(this.aeronave[fileira][letra], letra); // Calcula o valor da passagem
                this.aeronave[fileira][letra].addPassagens(1); // Atualiza o número de passagens do passageiro
                this.passagens++;
            } else {
                System.out.println("ASSENTO OCUPADO");
            }
        } else {
            System.out.println("Cliente já tem voo marcado nesta data.");
        }
    }

    // Troca a posição de dois passageiros na lista de embarque
    public void trocaPosicao(ArrayList<Passageiro> emb, int i, int j) {
        Passageiro aux = emb.get(i);
        emb.set(i, emb.get(j));
        emb.set(j, aux);
    }

    // Exibe a ordem de embarque dos passageiros
    public void exibeEmbarque() {
        int i = 1;
        System.out.println("[===ORDEM DE EMBARQUE===]");
        for (Passageiro it : passageiros) {
            System.out.println(i + " - " + it.getNome());
            i++;
        }
        System.out.println("");
    }

    // Ordena os passageiros para embarque com base em critérios (aniversário, idade, comorbidades)
    public void ordenaEmbarque() {
        for (int i = 0; i < passageiros.size(); i++) {
            for (int j = i + 1; j < passageiros.size(); j++) {
                if (!this.isAniversario(passageiros.get(i)) && this.isAniversario(passageiros.get(j))) {
                    trocaPosicao(this.passageiros, i, j);
                }
            }
        }
        for (int i = 0; i < passageiros.size(); i++) {
            for (int j = i + 1; j < passageiros.size(); j++) {
                if (passageiros.get(i).getIdade(hoje) < 60 && passageiros.get(j).getIdade(hoje) >= 60) {
                    trocaPosicao(this.passageiros, i, j);
                }
            }
        }
        for (int i = 0; i < passageiros.size(); i++) {
            for (int j = i + 1; j < passageiros.size(); j++) {
                if (!passageiros.get(i).isComorbidades() && passageiros.get(j).isComorbidades()) {
                    trocaPosicao(this.passageiros, i, j);
                }
            }
        }
    }

    // Calcula o preço da passagem com base no assento e descontos aplicáveis
    public double calcPassagem(Passageiro p, int letra) {
        double passagem = 0.00;
        switch (letra) {
            case 0:
                passagem += 850.00;
                break;
            case 1:
                passagem += 550.00;
                break;
            case 2:
                passagem += 720.00;
                break;
        }
        double desconto = 0;
        if (p.getPassagens() > 0 && p.getPassagens() < 10) {
            desconto += p.getPassagens() * 0.05;
        } else if (p.getPassagens() >= 10) {
            desconto += 0.5;
        }
        if (p.isComorbidades()) {
            desconto += 0.15;
        }
        if (p.getIdade(this.hoje) >= 60) {
            desconto += 0.05;
        }
        if (this.isAniversario(p)) {
            desconto += 0.05;
        }
        return passagem - (passagem * desconto);
    }

    // Verifica se é o aniversário do passageiro
    public boolean isAniversario(Passageiro p) {
        DateTimeFormatter aux = DateTimeFormatter.ofPattern("dd/MM");
        String curDate = LocalDate.now().plusDays(this.hoje).format(aux);
        String aniv = p.getNascimento().format(aux);
        return curDate.equals(aniv);
    }

    // Busca passageiro pelo nome nos assentos
    public Passageiro buscaPassageiro() {
        String passageiro;
        Passageiro aux = null;
        passageiro = scanner.nextLine();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 3; j++) {
                if (passageiro.equalsIgnoreCase(this.aeronave[i][j].getNome())) {
                    aux = aeronave[i][j];
                }
            }
        }
        return aux;
    }

    // Busca passageiro na lista de passageiros
    public Passageiro buscaPassageiro(Passageiro p) {
        String aux = p.getNome();
        Passageiro temp = null;
        for (Passageiro it : this.passageiros) {
            if (it.getNome().equals(aux)) {
                temp = it;
            }
        }
        return temp;
    }

    // Retorna o faturamento total da aeronave
    public double getFaturamento() {
        return faturamento;
    }

    // Retorna a lista de passageiros
    public ArrayList<Passageiro> getPassageiros() {
        return passageiros;
    }

    // Retorna a data de partida formatada
    public String getPartida() {
        String dataPartida = this.partida.format(formatter);
        return dataPartida;
    }
}
