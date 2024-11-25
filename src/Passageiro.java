import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Passageiro {
    private Scanner scanner;                 // Scanner para entrada de dados
    private String nome, email, endereco;    // Informações básicas do passageiro
    private LocalDate nascimento;            // Data de nascimento
    private DateTimeFormatter formatador;    // Formato para manipulação de datas
    private boolean comorbidades;            // Indica se o passageiro possui comorbidades
    private int passagens;                   // Número total de passagens compradas

    // Construtor para entrada de dados via terminal
    public Passageiro() {
        this.passagens = 0;
        this.scanner = new Scanner(System.in);

        // Entrada de informações básicas do passageiro
        System.out.print("Nome: ");
        this.nome = this.scanner.nextLine();

        System.out.print("E-mail: ");
        this.email = this.scanner.nextLine();

        System.out.print("Endereco: ");
        this.endereco = this.scanner.nextLine();

        // Configuração do formato da data
        this.formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Data de nascimento: ");
        this.nascimento = LocalDate.parse(this.scanner.nextLine(), this.formatador);

        // Verificação se o passageiro possui comorbidades
        char aux;
        do {
            System.out.print("Comorbidades? (s/n): ");
            aux = this.scanner.next().charAt(0);
            if (aux == 's' || aux == 'S') {
                this.comorbidades = true;
            } else if (aux == 'n' || aux == 'N') {
                this.comorbidades = false;
            } else {
                System.out.println("Opcao invalida, tente novamente");
            }
        } while (aux != 'S' && aux != 's' && aux != 'n' && aux != 'N');
    }

    // Métodos getters e setters para os atributos
    public boolean isComorbidades() {
        return comorbidades;
    }


    public LocalDate getNascimento() {
        return nascimento;
    }

    public int getIdade(long dias) {
        // Calcula a idade do passageiro com base na data atual + deslocamento de dias
        return (int) ChronoUnit.YEARS.between(this.nascimento, LocalDate.now().plusDays(dias));
    }

    public String getEndereco() {
        return endereco;
    }


    public int getPassagens() {
        return passagens;
    }

    public void addPassagens(int passagens) {
        this.passagens += passagens;
    }

    public String getEmail() {

        return email;
    }


    public String getNome() {

        return nome;
    }
}
