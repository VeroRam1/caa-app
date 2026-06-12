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
        createLevel1Boards();
        createLevel2Boards();
        createLevel3Boards();
        log.info("DataInitializer: all default boards checked/created.");
    }

    // ── Nivel 1 ───────────────────────────────────────────────────────────────

    private void createLevel1Boards() {
        createBoardIfNotExists("Tablero Básico - Nivel 1",
                "Tablero predeterminado para comunicación básica.",
                3, 4, 1,
                List.of(
                        new long[]{6632,  0, 0},  // yo
                        new long[]{2458,  1, 0},  // mamá
                        new long[]{5584,  2, 0},  // sí
                        new long[]{6456,  3, 0},  // comer
                        new long[]{12281, 0, 1},  // tú
                        new long[]{31146, 1, 1},  // papá
                        new long[]{5526,  2, 1},  // no
                        new long[]{4768,  3, 1},  // beber
                        new long[]{36518, 0, 2},  // quiero
                        new long[]{38351, 1, 2},  // familia
                        new long[]{12264, 2, 2},  // mío
                        new long[]{3220,  3, 2}   // más
                ),
                List.of("Yo","Mamá","Sí","Comer",
                        "Tú","Papá","No","Beber",
                        "Quiero","Familia","Mío","Más")
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
                        new long[]{34027, 2, 2}, // sí
                        new long[]{26061, 3, 2}  // no
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
    }

    // ── Nivel 2 ───────────────────────────────────────────────────────────────

    private void createLevel2Boards() {
        createBoardIfNotExists("Tablero General - Nivel 2",
                "Tablero predeterminado para comunicación intermedia con construcción de frases.",
                4, 5, 2,
                List.of(
                        new long[]{6632,  0, 0}, // yo
                        new long[]{12281, 1, 0}, // tú
                        new long[]{2458,  2, 0}, // mamá
                        new long[]{31146, 3, 0}, // papá
                        new long[]{5584,  4, 0}, // sí
                        new long[]{8484,  0, 1}, // feliz
                        new long[]{33633, 1, 1}, // triste
                        new long[]{37045, 2, 1}, // dolor
                        new long[]{8490,  3, 1}, // enfadado
                        new long[]{5526,  4, 1}, // no
                        new long[]{2,     0, 2}, // casa
                        new long[]{5938,  1, 2}, // colegio
                        new long[]{7873,  2, 2}, // familia
                        new long[]{6038,  3, 2}, // parque
                        new long[]{6503,  4, 2}, // médico
                        new long[]{36518, 0, 3}, // quiero
                        new long[]{4892,  1, 3}, // mamá
                        new long[]{4907,  2, 3}, // papá
                        new long[]{5956,  3, 3}, // cansado
                        new long[]{3220,  4, 3}  // más
                ),
                List.of(
                        "yo","tú","mamá","papá","sí",
                        "feliz","triste","dolor","enfadado","no",
                        "casa","colegio","familia","parque","médico",
                        "quiero","mamá","papá","cansado","más"
                )
        );

        createBoardIfNotExists("Alimentos - Nivel 2",
                "Vocabulario ampliado de alimentos y bebidas.",
                4, 5, 2,
                List.of(
                        new long[]{2454,  0, 0}, // agua
                        new long[]{7841,  1, 0}, // comida
                        new long[]{4341,  2, 0}, // leche
                        new long[]{4910,  3, 0}, // pan
                        new long[]{4389,  4, 0}, // zumo
                        new long[]{5264,  0, 1}, // fruta
                        new long[]{4336,  1, 1}, // galleta
                        new long[]{5262,  2, 1}, // verdura
                        new long[]{4429,  3, 1}, // arroz
                        new long[]{4372,  4, 1}, // pasta
                        new long[]{4385,  0, 2}, // yogur
                        new long[]{4338,  1, 2}, // huevo
                        new long[]{4906,  2, 2}, // pollo
                        new long[]{4897,  3, 2}, // pescado
                        new long[]{4387,  4, 2}, // queso
                        new long[]{4384,  0, 3}, // tomate
                        new long[]{4334,  1, 3}, // manzana
                        new long[]{36518, 2, 3}, // quiero
                        new long[]{34027, 3, 3}, // sí
                        new long[]{26061, 4, 3}  // no
                ),
                List.of(
                        "agua","comida","leche","pan","zumo",
                        "fruta","galleta","verdura","arroz","pasta",
                        "yogur","huevo","pollo","pescado","queso",
                        "tomate","manzana","quiero","sí","no"
                )
        );

        createBoardIfNotExists("Emociones - Nivel 2",
                "Vocabulario ampliado de emociones y estados de ánimo.",
                4, 5, 2,
                List.of(
                        new long[]{8484,  0, 0}, // feliz
                        new long[]{33633, 1, 0}, // triste
                        new long[]{37045, 2, 0}, // dolor
                        new long[]{8490,  3, 0}, // enfadado
                        new long[]{8491,  4, 0}, // miedo
                        new long[]{8489,  0, 1}, // sorprendido
                        new long[]{8488,  1, 1}, // aburrido
                        new long[]{5956,  2, 1}, // cansado
                        new long[]{8492,  3, 1}, // nervioso
                        new long[]{8498,  4, 1}, // tranquilo
                        new long[]{34027, 0, 2}, // sí
                        new long[]{26061, 1, 2}, // no
                        new long[]{11367, 2, 2}, // ayuda
                        new long[]{2454,  3, 2}, // agua
                        new long[]{7841,  4, 2}, // comida
                        new long[]{36518, 0, 3}, // quiero
                        new long[]{6632,  1, 3}, // yo
                        new long[]{12281, 2, 3}, // tú
                        new long[]{3220,  3, 3}, // más
                        new long[]{12264, 4, 3}  // mío
                ),
                List.of(
                        "feliz","triste","dolor","enfadado","miedo",
                        "sorprendido","aburrido","cansado","nervioso","tranquilo",
                        "sí","no","ayuda","agua","comida",
                        "quiero","yo","tú","más","mío"
                )
        );

        createBoardIfNotExists("Acciones - Nivel 2",
                "Verbos y acciones cotidianas básicas.",
                4, 5, 2,
                List.of(
                        new long[]{6456,  0, 0}, // comer
                        new long[]{4768,  1, 0}, // beber
                        new long[]{4884,  2, 0}, // dormir
                        new long[]{4886,  3, 0}, // jugar
                        new long[]{4888,  4, 0}, // leer
                        new long[]{4889,  0, 1}, // escribir
                        new long[]{4890,  1, 1}, // dibujar
                        new long[]{4893,  2, 1}, // bailar
                        new long[]{4894,  3, 1}, // correr
                        new long[]{4895,  4, 1}, // saltar
                        new long[]{4898,  0, 2}, // ver
                        new long[]{4899,  1, 2}, // escuchar
                        new long[]{4900,  2, 2}, // hablar
                        new long[]{5781,  3, 2}, // baño
                        new long[]{11367, 4, 2}, // ayuda
                        new long[]{36518, 0, 3}, // quiero
                        new long[]{6632,  1, 3}, // yo
                        new long[]{34027, 2, 3}, // sí
                        new long[]{26061, 3, 3}, // no
                        new long[]{3220,  4, 3}  // más
                ),
                List.of(
                        "comer","beber","dormir","jugar","leer",
                        "escribir","dibujar","bailar","correr","saltar",
                        "ver","escuchar","hablar","baño","ayuda",
                        "quiero","yo","sí","no","más"
                )
        );
    }

    // ── Nivel 3 ───────────────────────────────────────────────────────────────

    private void createLevel3Boards() {
        createBoardIfNotExists("Tablero General - Nivel 3",
                "Tablero predeterminado para comunicación avanzada con vocabulario extenso.",
                5, 6, 3,
                List.of(
                        new long[]{6632,  0, 0}, new long[]{12281, 1, 0}, new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0}, new long[]{5584,  4, 0}, new long[]{5526,  5, 0},
                        new long[]{8484,  0, 1}, new long[]{33633, 1, 1}, new long[]{37045, 2, 1},
                        new long[]{8490,  3, 1}, new long[]{8491,  4, 1}, new long[]{5956,  5, 1},
                        new long[]{2,     0, 2}, new long[]{5938,  1, 2}, new long[]{7873,  2, 2},
                        new long[]{6038,  3, 2}, new long[]{6503,  4, 2}, new long[]{7056,  5, 2},
                        new long[]{4892,  0, 3}, new long[]{4907,  1, 3}, new long[]{5062,  2, 3},
                        new long[]{5001,  3, 3}, new long[]{5003,  4, 3}, new long[]{5108,  5, 3},
                        new long[]{6456,  0, 4}, new long[]{4886,  1, 4}, new long[]{4884,  2, 4},
                        new long[]{4888,  3, 4}, new long[]{4898,  4, 4}, new long[]{4900,  5, 4}
                ),
                List.of(
                        "yo","tú","mamá","papá","sí","no",
                        "feliz","triste","dolor","enfadado","miedo","cansado",
                        "casa","colegio","familia","parque","médico","tienda",
                        "mamá","papá","amigo","abuelo","abuela","profesor",
                        "comer","jugar","dormir","leer","ver","hablar"
                )
        );

        createBoardIfNotExists("Acciones - Nivel 3",
                "Vocabulario ampliado de verbos y acciones.",
                5, 6, 3,
                List.of(
                        new long[]{6456,  0, 0}, new long[]{4768,  1, 0}, new long[]{4884,  2, 0},
                        new long[]{4886,  3, 0}, new long[]{4888,  4, 0}, new long[]{4889,  5, 0},
                        new long[]{4890,  0, 1}, new long[]{4891,  1, 1}, new long[]{4893,  2, 1},
                        new long[]{4894,  3, 1}, new long[]{4895,  4, 1}, new long[]{4896,  5, 1},
                        new long[]{4898,  0, 2}, new long[]{4899,  1, 2}, new long[]{4900,  2, 2},
                        new long[]{5781,  3, 2}, new long[]{11367, 4, 2}, new long[]{2454,  5, 2},
                        new long[]{36518, 0, 3}, new long[]{6632,  1, 3}, new long[]{12281, 2, 3},
                        new long[]{34027, 3, 3}, new long[]{26061, 4, 3}, new long[]{3220,  5, 3},
                        new long[]{8484,  0, 4}, new long[]{33633, 1, 4}, new long[]{8490,  2, 4},
                        new long[]{5956,  3, 4}, new long[]{8491,  4, 4}, new long[]{12264, 5, 4}
                ),
                List.of(
                        "comer","beber","dormir","jugar","leer","escribir",
                        "dibujar","cantar","bailar","correr","saltar","nadar",
                        "ver","escuchar","hablar","baño","ayuda","agua",
                        "quiero","yo","tú","sí","no","más",
                        "feliz","triste","enfadado","cansado","miedo","mío"
                )
        );

        createBoardIfNotExists("Emociones - Nivel 3",
                "Vocabulario completo de emociones y estados de ánimo.",
                5, 6, 3,
                List.of(
                        new long[]{8484,  0, 0}, new long[]{33633, 1, 0}, new long[]{37045, 2, 0},
                        new long[]{8490,  3, 0}, new long[]{8491,  4, 0}, new long[]{5956,  5, 0},
                        new long[]{8489,  0, 1}, new long[]{8488,  1, 1}, new long[]{8492,  2, 1},
                        new long[]{8498,  3, 1}, new long[]{8493,  4, 1}, new long[]{8494,  5, 1},
                        new long[]{8495,  0, 2}, new long[]{8496,  1, 2}, new long[]{8497,  2, 2},
                        new long[]{34027, 3, 2}, new long[]{26061, 4, 2}, new long[]{11367, 5, 2},
                        new long[]{36518, 0, 3}, new long[]{6632,  1, 3}, new long[]{12281, 2, 3},
                        new long[]{2454,  3, 3}, new long[]{7841,  4, 3}, new long[]{3220,  5, 3},
                        new long[]{2,     0, 4}, new long[]{5938,  1, 4}, new long[]{6038,  2, 4},
                        new long[]{4892,  3, 4}, new long[]{4907,  4, 4}, new long[]{5062,  5, 4}
                ),
                List.of(
                        "feliz","triste","dolor","enfadado","miedo","cansado",
                        "sorprendido","aburrido","nervioso","tranquilo","asustado","avergonzado",
                        "orgulloso","celoso","confundido","sí","no","ayuda",
                        "quiero","yo","tú","agua","comida","más",
                        "casa","colegio","parque","mamá","papá","amigo"
                )
        );

        createBoardIfNotExists("Alimentos - Nivel 3",
                "Vocabulario completo de alimentos y bebidas.",
                5, 6, 3,
                List.of(
                        new long[]{2454,  0, 0}, new long[]{7841,  1, 0}, new long[]{4341,  2, 0},
                        new long[]{4910,  3, 0}, new long[]{4389,  4, 0}, new long[]{5264,  5, 0},
                        new long[]{4336,  0, 1}, new long[]{5262,  1, 1}, new long[]{4429,  2, 1},
                        new long[]{4372,  3, 1}, new long[]{4385,  4, 1}, new long[]{4338,  5, 1},
                        new long[]{4906,  0, 2}, new long[]{4897,  1, 2}, new long[]{4387,  2, 2},
                        new long[]{4384,  3, 2}, new long[]{4334,  4, 2}, new long[]{4340,  5, 2},
                        new long[]{4903,  0, 3}, new long[]{4902,  1, 3}, new long[]{4901,  2, 3},
                        new long[]{4904,  3, 3}, new long[]{4905,  4, 3}, new long[]{4908,  5, 3},
                        new long[]{36518, 0, 4}, new long[]{6632,  1, 4}, new long[]{34027, 2, 4},
                        new long[]{26061, 3, 4}, new long[]{3220,  4, 4}, new long[]{11367, 5, 4}
                ),
                List.of(
                        "agua","comida","leche","pan","zumo","fruta",
                        "galleta","verdura","arroz","pasta","yogur","huevo",
                        "pollo","pescado","queso","tomate","manzana","jamón",
                        "zanahoria","patata","naranja","plátano","pera","uva",
                        "quiero","yo","sí","no","más","ayuda"
                )
        );
    }

    // ── Helper ────────────────────────────────────────────────────────────────

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
