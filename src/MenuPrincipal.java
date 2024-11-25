import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// Classe principal responsável por gerenciar o menu e as operações do sistema
public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in); // Scanner para entrada de dados do usuário
    private int opc; // Opção escolhida pelo usuário no menu
    private DateTimeFormatter formatter; // Formata datas no padrão "dd/MM/yyyy"
    private ArrayList<Aeronave> aeronaves; // Lista de aeronaves disponíveis
    private ArrayList<Passageiro> clientes; // Lista de passageiros cadastrados
    private ArrayList<VooConcluido> partidas; // Lista de voos realizados
    int curDate; // Representa o dia atual no contexto do sistema

    // Construtor da classe
    public MenuPrincipal() {
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define o formato de data
        this.aeronaves = new ArrayList<Aeronave>(); // Inicializa a lista de aeronaves
        this.clientes = new ArrayList<Passageiro>(); // Inicializa a lista de passageiros
        this.partidas = new ArrayList<VooConcluido>(); // Inicializa a lista de voos realizados
        this.opc = 0; // Inicializa a opção do menu como 0
        this.curDate = 0; // Define o dia atual como 0

        // Preenche a lista de aeronaves com voos programados para os próximos 31 dias
        for (int i = 0; i < 31; i++) {
            aeronaves.add(new Aeronave(LocalDate.now().plusDays(i), i));
        }

        // Loop principal do menu
        do {
            System.out.println("1 - Calendário de viagens");
            System.out.println("2 - Cadastrar novo cliente");
            System.out.println("3 - Vender passagem");
            System.out.println("4 - Decolar (avançar dia)");
            System.out.println("5 - Voos realizados");
            System.out.println("0 - Sair");
            this.opc = this.scanner.nextInt(); // Lê a opção do usuário
            this.scanner.nextLine(); // Consome o caractere de nova linha

            // Executa a ação correspondente à opção escolhida
            switch (opc) {
                case 1:
                    this.calendarioViagens();
                    break;
                case 2:
                    this.clientes.add(new Passageiro());
                    break;
                case 3:
                    this.vendePassagem();
                    break;
                case 4:
                    this.embarqueDoDia();
                    break;
                case 5:
                    this.voosRealizados();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        } while (opc != 0); // Sai do loop quando a opção for 0
    }

    // Exibe os voos realizados até o momento
    private void voosRealizados() {
        for (VooConcluido it : this.partidas) {
            String data = it.getData().format(formatter);
            System.out.println("------------+ " + data + " +------------");
            System.out.println("Faturamento: R$" + it.getFaturamento());
            System.out.println("------------+------------+------------");
        }
    }

    // Processa o embarque do dia atual
    public void embarqueDoDia() {
        if (!this.aeronaves.get(0).getPassageiros().isEmpty()) {
            this.aeronaves.get(0).ordenaEmbarque(); // Ordena os passageiros
            this.aeronaves.get(0).exibeEmbarque(); // Exibe os passageiros prontos para embarque
            this.partidas.add(new VooConcluido(
                    this.aeronaves.get(0).getFaturamento(), LocalDate.now().plusDays(this.curDate)
            )); // Adiciona o voo à lista de partidas
            String departure = this.partidas.get(curDate).getData().format(formatter);
            System.out.println("Avião decolou em: " + departure);
            System.out.println("Faturamento em passagens: R$" + this.partidas.get(curDate).getFaturamento());
            this.curDate++;
            this.limpaVoo(this.aeronaves.get(0)); // Limpa os passageiros do voo
            this.aeronaves.remove(0); // Remove a aeronave que decolou
            aeronaves.add(new Aeronave(LocalDate.now().plusDays(this.curDate + 30), curDate)); // Adiciona um novo voo
        } else {
            this.partidas.add(new VooConcluido(
                    this.aeronaves.get(0).getFaturamento(), LocalDate.now().plusDays(this.curDate)
            )); // Registra o voo como não realizado
            this.aeronaves.remove(0);
            System.out.println("Voo não foi iniciado devido à falta de passageiros");
            this.curDate++;
            aeronaves.add(new Aeronave(LocalDate.now().plusDays(this.curDate + 30), curDate)); // Adiciona um novo voo
        }
        System.out.println("------------+------------+------------");
    }

    // Remove os passagens dos passegeiros do voo que decolou para fins de calculo de desconto
    private void limpaVoo(Aeronave a) {
        ArrayList<Passageiro> passageiros = a.getPassageiros();
        for (Passageiro it : passageiros) {
            it.addPassagens(-1);
        }
    }

    // Exibe o calendário de viagens
    public void calendarioViagens() {
        for (Aeronave it : aeronaves) {
            System.out.println(it.getPartida());
        }
    }

    // Busca um cliente pelo nome
    public Passageiro buscaCliente() {
        String entrada;
        Passageiro aux = null;
        System.out.print("Nome: ");
        entrada = this.scanner.nextLine();
        for (Passageiro it : clientes) {
            if (entrada.equalsIgnoreCase(it.getNome())) {
                aux = it;
            }
        }
        return aux;
    }

    // Vende uma passagem para um cliente
    public void vendePassagem() {
        Passageiro p;
        do {
            p = buscaCliente(); // Busca o cliente pelo nome
            if (p == null) {
                System.out.println("Cliente não encontrado!");
            }
        } while (p == null);

        Aeronave aux = buscaViagem(); // Busca a aeronave para a data desejada
        if (aux == null) {
            System.out.println("Não temos aeronaves disponíveis para esta data");
        } else {
            aux.novoPassageiro(p); // Adiciona o passageiro à aeronave
        }
    }

    // Busca uma aeronave por data
    public Aeronave buscaViagem() {
        Aeronave aux = null;
        System.out.print("Data da passagem (dd/MM/yyyy): ");
        String dataViagem = this.scanner.nextLine();
        for (Aeronave it : aeronaves) {
            if (it.getPartida().equals(dataViagem)) {
                aux = it;
            }
        }
        return aux;
    }
}
