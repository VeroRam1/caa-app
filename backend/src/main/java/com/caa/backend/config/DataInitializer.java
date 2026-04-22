package com.caa.backend.config;

import com.caa.backend.model.BoardPictogram;
import com.caa.backend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.caa.backend.model.Board;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final BoardRepository boardRepository;

    @Override
    public void run(String... args) {
        createDefaultLevel1Board();
    }

    /**
     * Creates the default Level 1 board with 12 pictograms (3 rows x 4 columns).
     * Each entry: { arasaacId, labelText, positionX, positionY }
     */
    private void createDefaultLevel1Board() {
        boolean exists = boardRepository.existsByName("Tablero Básico - Nivel 1");
        if (exists) {
            log.info("Default Level 1 board already exists, skipping creation.");
            return;
        }

        log.info("Creating default Level 1 board...");


        createBoardIfNotExists("Tablero Básico - Nivel 1",
                "Tablero predeterminado para comunicación básica. " ,
                3, 4, 1,
                List.of(
                        // Row 0
                        new long[]{6632,  0, 0},  // yo
                        new long[]{2458,  1, 0},  // mamá
                        new long[]{5584,  2, 0},  // sí
                        new long[]{6456, 3, 0},  // comer

                        // Row 1
                        new long[]{12281,  0, 1},  // tú
                        new long[]{31146, 1, 1},  // papa
                        new long[]{5526, 2, 1},  // no
                        new long[]{4768, 3, 1},  // beber

                        // Row 2
                        new long[]{36518, 0, 2},  // quiero
                        new long[]{38351,     1, 2},  // familia
                        new long[]{12264,  2, 2},  // mío
                        new long[]{3220,  3, 2}   // más
                ),
                List.of("Yo", "Mamá", "Sí", "Comer",
                        "Tú", "Papá", "No", "Beber",
                        "Quiero", "Familia", "Mío", "Más")
        );


        createBoardIfNotExists("Alimentos - Nivel 1",
                "Vocabulario de alimentos y bebidas básicos.",
                3, 4, 1,
                List.of(
                        new long[]{2454,  0, 0}, // agua
                        new long[]{7841,  1, 0}, // comida
                        new long[]{4341,  2, 0}, // leche
                        new long[]{4910,  3, 0}, // pan
                        new long[]{5264,  0, 1}, // fruta
                        new long[]{4336,  1, 1}, // galleta
                        new long[]{4389,  2, 1}, // zumo
                        new long[]{5262,  3, 1}, // verdura
                        new long[]{4429,  0, 2}, // arroz
                        new long[]{4372,  1, 2}, // pasta
                        new long[]{34027, 2, 2}, // sí (quiero)
                        new long[]{26061, 3, 2}  // no (quiero)
                ),
                List.of("agua","comida","leche","pan",
                        "fruta","galleta","zumo","verdura",
                        "arroz","pasta","sí","no")
        );

        createBoardIfNotExists("Emociones - Nivel 1",
                "Vocabulario de emociones y estados de ánimo básicos.",
                3, 4, 1,
                List.of(
                        new long[]{8484,  0, 0}, // feliz
                        new long[]{33633, 1, 0}, // triste
                        new long[]{37045, 2, 0}, // dolor
                        new long[]{5956,  3, 0}, // cansado
                        new long[]{8490,  0, 1}, // enfadado
                        new long[]{8491,  1, 1}, // miedo
                        new long[]{8489,  2, 1}, // sorprendido
                        new long[]{8488,  3, 1}, // aburrido
                        new long[]{34027, 0, 2}, // sí
                        new long[]{26061, 1, 2}, // no
                        new long[]{11367, 2, 2}, // ayuda
                        new long[]{2454,  3, 2}  // agua
                ),
                List.of("feliz","triste","dolor","cansado",
                        "enfadado","miedo","sorprendido","aburrido",
                        "sí","no","ayuda","agua")
        );

        createBoardIfNotExists("Lugares - Nivel 1",
                "Vocabulario de lugares cotidianos del niño.",
                3, 4, 1,
                List.of(
                        new long[]{2,     0, 0}, // casa
                        new long[]{5938,  1, 0}, // colegio
                        new long[]{5781,  2, 0}, // baño
                        new long[]{6038,  3, 0}, // parque
                        new long[]{6023,  0, 1}, // habitación
                        new long[]{6021,  1, 1}, // cocina
                        new long[]{6503,  2, 1}, // médico
                        new long[]{7056,  3, 1}, // tienda
                        new long[]{34027, 0, 2}, // sí
                        new long[]{26061, 1, 2}, // no
                        new long[]{11367, 2, 2}, // ayuda
                        new long[]{2454,  3, 2}  // agua
                ),
                List.of("casa","colegio","baño","parque",
                        "habitación","cocina","médico","tienda",
                        "sí","no","ayuda","agua")
        );

        createBoardIfNotExists("Personas - Nivel 1",
                "Vocabulario de personas del entorno cercano del niño.",
                3, 4, 1,
                List.of(
                        new long[]{7873,  0, 0}, // familia
                        new long[]{4892,  1, 0}, // mamá
                        new long[]{4907,  2, 0}, // papá
                        new long[]{4885,  3, 0}, // hermano
                        new long[]{5001,  0, 1}, // abuelo
                        new long[]{5003,  1, 1}, // abuela
                        new long[]{5062,  2, 1}, // amigo
                        new long[]{5108,  3, 1}, // profesor
                        new long[]{34027, 0, 2}, // sí
                        new long[]{26061, 1, 2}, // no
                        new long[]{11367, 2, 2}, // ayuda
                        new long[]{2454,  3, 2}  // agua
                ),
                List.of("familia","mamá","papá","hermano",
                        "abuelo","abuela","amigo","profesor",
                        "sí","no","ayuda","agua")
        );

        log.info("DataInitializer: all default boards checked/created.");
    }

    private void createBoardIfNotExists(
            String name, String description,
            int rows, int columns, int level,
            List<long[]> positions, List<String> labels) {

        if (boardRepository.existsByName(name)) {
            log.info("Board '{}' already exists, skipping.", name);
            return;
        }

        Board board = new Board();
        board.setName(name);
        board.setDescription(description);
        board.setRows(rows);
        board.setColumns(columns);
        board.setLevel(level);
        board.setIsPredefined(true);

        for (int i = 0; i < positions.size(); i++) {
            long[] pos = positions.get(i);
            BoardPictogram p = new BoardPictogram();
            p.setPictogramId((int) pos[0]);
            p.setPictogramUrl(buildArasaacUrl((int) pos[0]));
            p.setText(labels.get(i));
            p.setPositionX((int) pos[1]);
            p.setPositionY((int) pos[2]);
            p.setBoard(board);
            board.addPictogram(p);
        }

        boardRepository.save(board);
        log.info("Board '{}' created with {} pictograms.", name, positions.size());
    }

    private String buildArasaacUrl(int id) {
        return String.format(
                "https://static.arasaac.org/pictograms/%d/%d_500.png", id, id);
    }


}
