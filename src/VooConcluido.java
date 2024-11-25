import java.time.LocalDate;

public class VooConcluido {
    private double faturamento = 0; // Armazena o faturamento total do voo
    private LocalDate data;        // Data em que o voo foi concluído

    // Construtor que inicializa os valores de faturamento e data
    public VooConcluido(double faturamento, LocalDate data) {
        this.faturamento = faturamento;
        this.data = data;
    }

    // Método getter para acessar o faturamento do voo concluído
    public double getFaturamento() {
        return faturamento;
    }

    // Método getter para acessar a data do voo concluído
    public LocalDate getData() {
        return data;
    }
}
