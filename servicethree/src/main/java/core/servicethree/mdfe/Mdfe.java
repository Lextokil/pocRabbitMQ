package core.servicethree.mdfe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Mdfe {

    @Id
    private String id;
    private String chaveAcesso;
    private LocalDate dataProcessamento;
    private String cod;

}
