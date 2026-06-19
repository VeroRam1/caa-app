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
        createExtraBoards();
        log.info("DataInitializer: all default boards checked/created.");
    }

    // ** Level 1 ***********************************************

    private void createLevel1Boards() {
        createBoardIfNotExists("Tablero Básico - Nivel 1",
                "Tablero predeterminado para comunicación básica.",
                3, 4, 1, "General",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{2458,  1, 0},
                        new long[]{31146,  2, 0},
                        new long[]{5584,  3, 0},
                        new long[]{12264, 0, 1},
                        new long[]{6456, 1, 1},
                        new long[]{6061,  2, 1},
                        new long[]{5526,  3, 1},
                        new long[]{36518, 0, 2},
                        new long[]{38351, 1, 2},
                        new long[]{12281, 2, 2},
                        new long[]{3220,  3, 2}
                ),
                List.of("Yo","Mamá","Papá","Sí",
                        "Mío","Comer","Beber","No",
                        "Quiero","Familia","Tú","Más")
        );

        createBoardIfNotExists("Alimentos - Nivel 1",
                "Vocabulario de alimentos y bebidas básicos.",
                3, 4, 1, "Alimentos",
                List.of(
                        new long[]{6456,  0, 0},
                        new long[]{2248,  1, 0},
                        new long[]{11461, 2, 0},
                        new long[]{2445,  3, 0},
                        new long[]{28339, 0, 1},
                        new long[]{2462,  1, 1},
                        new long[]{2530,  2, 1},
                        new long[]{6525,  3, 1},
                        new long[]{2573,  0, 2},
                        new long[]{2534,  1, 2},
                        new long[]{2316,  2, 2},
                        new long[]{2494,  3, 2}
                ),
                List.of(
                        "comer", "agua", "zumo", "leche",
                        "fruta", "manzana", "plátano", "galletas",
                        "sopa", "muslo", "carne", "pan"
                )
        );

        createBoardIfNotExists("Emociones - Nivel 1",
                "Vocabulario de emociones y estados de ánimo básicos.",
                2, 4, 1, "Emociones",
                List.of(
                        new long[]{3245,  0, 0},
                        new long[]{2606,  1, 0},
                        new long[]{2367,  2, 0},
                        new long[]{2314,  3, 0},
                        new long[]{2374,  0, 1},
                        new long[]{2261,  1, 1},
                        new long[]{2574,  2, 1},
                        new long[]{2245,  3, 1}
                ),
                List.of(
                        "contento", "triste", "dolor", "cansado",
                        "enfadado", "asustado", "sorprendido", "aburrido"
                )
        );

        createBoardIfNotExists("Lugares - Nivel 1",
                "Vocabulario de lugares cotidianos del niño.",
                2, 3, 1, "Lugares",
                List.of(
                        new long[]{6964,  0, 0},
                        new long[]{3082,  1, 0},
                        new long[]{2859,  2, 0},
                        new long[]{33068, 0, 1},
                        new long[]{15905, 1, 1},
                        new long[]{33074, 2, 1}
                ),
                List.of(
                        "casa", "colegio", "parque",
                        "dormitorio", "baño", "salón"
                )
        );

        createBoardIfNotExists("Personas - Nivel 1",
                "Vocabulario de personas del entorno cercano del niño.",
                2, 4, 1, "Personas",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{6625,  1, 0},
                        new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{23718, 0, 1},
                        new long[]{23710, 1, 1},
                        new long[]{2457,  2, 1},
                        new long[]{2456,  3, 1}
                ),
                List.of(
                        "yo", "tú", "mamá", "papá",
                        "abuelo", "abuela", "maestro", "maestra"
                )
        );
    }

    // ** Level 2 ***********************************************
    private void createLevel2Boards() {
        createBoardIfNotExists("Tablero General - Nivel 2",
                "Tablero predeterminado para comunicación intermedia con construcción de frases.",
                4, 5, 2, "General",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{2458,  1, 0},
                        new long[]{31146, 2, 0},
                        new long[]{5584,  3, 0},
                        new long[]{5584,  4, 0},
                        new long[]{12264, 0, 1},
                        new long[]{6456,  1, 1},
                        new long[]{6061,  2, 1},
                        new long[]{5526,  3, 1},
                        new long[]{5526,  4, 1},
                        new long[]{36518, 0, 2},
                        new long[]{38351, 1, 2},
                        new long[]{12281, 2, 2},
                        new long[]{3220,  3, 2},
                        new long[]{6503,  4, 2},
                        new long[]{36518, 0, 3},
                        new long[]{4892,  1, 3},
                        new long[]{4907,  2, 3},
                        new long[]{5956,  3, 3},
                        new long[]{3220,  4, 3}
                ),
                List.of(
                        "Yo", "Mamá", "Papá", "Sí", "sí",
                        "Mío", "Comer", "Beber", "No", "no",
                        "Quiero", "Familia", "Tú", "Más", "médico",
                        "quiero", "mamá", "papá", "cansado", "más"
                )
        );

        createBoardIfNotExists("Alimentos - Nivel 2",
                "Vocabulario ampliado de alimentos y bebidas.",
                4, 5, 2, "Alimentos",
                List.of(
                        new long[]{6456,  0, 0},
                        new long[]{2248,  1, 0},
                        new long[]{11461, 2, 0},
                        new long[]{2445,  3, 0},
                        new long[]{4389,  4, 0},
                        new long[]{28339, 0, 1},
                        new long[]{2462,  1, 1},
                        new long[]{2530,  2, 1},
                        new long[]{6525,  3, 1},
                        new long[]{4372,  4, 1},
                        new long[]{2573,  0, 2},
                        new long[]{2534,  1, 2},
                        new long[]{2316,  2, 2},
                        new long[]{2494,  3, 2},
                        new long[]{4387,  4, 2},
                        new long[]{4384,  0, 3},
                        new long[]{4334,  1, 3},
                        new long[]{36518, 2, 3},
                        new long[]{34027, 3, 3},
                        new long[]{26061, 4, 3}
                ),
                List.of(
                        "comer", "agua", "zumo", "leche", "zumo",
                        "fruta", "manzana", "plátano", "galletas", "pasta",
                        "sopa", "muslo", "carne", "pan", "queso",
                        "tomate", "manzana", "quiero", "sí", "no"
                )
        );

        createBoardIfNotExists("Emociones - Nivel 2",
                "Vocabulario ampliado de emociones y estados de ánimo.",
                4, 5, 2, "Emociones",
                List.of(
                        new long[]{3245,  0, 0},
                        new long[]{2606,  1, 0},
                        new long[]{2367,  2, 0},
                        new long[]{2314,  3, 0},
                        new long[]{8491,  4, 0},
                        new long[]{2374,  0, 1},
                        new long[]{2261,  1, 1},
                        new long[]{2574,  2, 1},
                        new long[]{2245,  3, 1},
                        new long[]{8498,  4, 1},
                        new long[]{34027, 0, 2},
                        new long[]{26061, 1, 2},
                        new long[]{11367, 2, 2},
                        new long[]{2454,  3, 2},
                        new long[]{7841,  4, 2},
                        new long[]{36518, 0, 3},
                        new long[]{6632,  1, 3},
                        new long[]{12281, 2, 3},
                        new long[]{3220,  3, 3},
                        new long[]{12264, 4, 3}
                ),
                List.of(
                        "contento", "triste", "dolor", "cansado", "miedo",
                        "enfadado", "asustado", "sorprendido", "aburrido", "tranquilo",
                        "sí", "no", "ayuda", "agua", "comida",
                        "quiero", "yo", "tú", "más", "mío"
                )
        );

        createBoardIfNotExists("Acciones - Nivel 2",
                "Verbos y acciones cotidianas básicas.",
                4, 5, 2, "Acciones",
                List.of(
                        new long[]{6456,  0, 0}, new long[]{4768,  1, 0}, new long[]{4884,  2, 0}, new long[]{4886,  3, 0}, new long[]{4888,  4, 0},
                        new long[]{4889,  0, 1}, new long[]{4890,  1, 1}, new long[]{4893,  2, 1}, new long[]{4894,  3, 1}, new long[]{4895,  4, 1},
                        new long[]{4898,  0, 2}, new long[]{4899,  1, 2}, new long[]{4900,  2, 2}, new long[]{5781,  3, 2}, new long[]{11367, 4, 2},
                        new long[]{36518, 0, 3}, new long[]{6632,  1, 3}, new long[]{34027, 2, 3}, new long[]{26061, 3, 3}, new long[]{3220,  4, 3}
                ),
                List.of(
                        "comer","beber","dormir","jugar","leer",
                        "escribir","dibujar","bailar","correr","saltar",
                        "ver","escuchar","hablar","baño","ayuda",
                        "quiero","yo","sí","no","más"
                )
        );
    }

    // ** Level 3 ***********************************************
    private void createLevel3Boards() {
        createBoardIfNotExists("Tablero General - Nivel 3",
                "Tablero predeterminado para comunicación avanzada con vocabulario extenso.",
                5, 6, 3, "General",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{2458,  1, 0},
                        new long[]{31146, 2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{5584,  4, 0},
                        new long[]{5526,  5, 0},
                        new long[]{12264, 0, 1},
                        new long[]{6456,  1, 1},
                        new long[]{6061,  2, 1},
                        new long[]{5526,  3, 1},
                        new long[]{8491,  4, 1},
                        new long[]{5956,  5, 1},
                        new long[]{36518, 0, 2},
                        new long[]{38351, 1, 2},
                        new long[]{12281, 2, 2},
                        new long[]{3220,  3, 2},
                        new long[]{6503,  4, 2},
                        new long[]{7056,  5, 2},
                        new long[]{4892,  0, 3}, new long[]{4907,  1, 3}, new long[]{5062,  2, 3}, new long[]{5001,  3, 3}, new long[]{5003,  4, 3}, new long[]{5108,  5, 3},
                        new long[]{6456,  0, 4}, new long[]{4886,  1, 4}, new long[]{4884,  2, 4}, new long[]{4888,  3, 4}, new long[]{4898,  4, 4}, new long[]{4900,  5, 4}
                ),
                List.of(
                        "Yo", "Mamá", "Papá", "papá", "sí", "no",
                        "Mío", "Comer", "Beber", "No", "miedo", "cansado",
                        "Quiero", "Familia", "Tú", "Más", "médico", "tienda",
                        "mamá", "papá", "amigo", "abuelo", "abuela", "profesor",
                        "comer", "jugar", "dormir", "leer", "ver", "hablar"
                )
        );

        createBoardIfNotExists("Acciones - Nivel 3",
                "Vocabulario ampliado de verbos y acciones.",
                5, 6, 3, "Acciones",
                List.of(
                        new long[]{6456,  0, 0}, new long[]{4768,  1, 0}, new long[]{4884,  2, 0}, new long[]{4886,  3, 0}, new long[]{4888,  4, 0}, new long[]{4889,  5, 0},
                        new long[]{4890,  0, 1}, new long[]{4891,  1, 1}, new long[]{4893,  2, 1}, new long[]{4894,  3, 1}, new long[]{4895,  4, 1}, new long[]{4896,  5, 1},
                        new long[]{4898,  0, 2}, new long[]{4899,  1, 2}, new long[]{4900,  2, 2}, new long[]{5781,  3, 2}, new long[]{11367, 4, 2}, new long[]{2454,  5, 2},
                        new long[]{36518, 0, 3}, new long[]{6632,  1, 3}, new long[]{12281, 2, 3}, new long[]{34027, 3, 3}, new long[]{26061, 4, 3}, new long[]{3220,  5, 3},
                        new long[]{8484,  0, 4}, new long[]{33633, 1, 4}, new long[]{8490,  2, 4}, new long[]{5956,  3, 4}, new long[]{8491,  4, 4}, new long[]{12264, 5, 4}
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
                5, 6, 3, "Emociones",
                List.of(
                        new long[]{3245,  0, 0},
                        new long[]{2606,  1, 0},
                        new long[]{2367,  2, 0},
                        new long[]{2314,  3, 0},
                        new long[]{8491,  4, 0},
                        new long[]{5956,  5, 0},
                        new long[]{2374,  0, 1},
                        new long[]{2261,  1, 1},
                        new long[]{2574,  2, 1},
                        new long[]{2245,  3, 1},
                        new long[]{8493,  4, 1},
                        new long[]{8494,  5, 1},
                        new long[]{8495,  0, 2}, new long[]{8496,  1, 2}, new long[]{8497,  2, 2}, new long[]{34027, 3, 2}, new long[]{26061, 4, 2}, new long[]{11367, 5, 2},
                        new long[]{36518, 0, 3}, new long[]{6632,  1, 3}, new long[]{12281, 2, 3}, new long[]{2454,  3, 3}, new long[]{7841,  4, 3}, new long[]{3220,  5, 3},
                        new long[]{2,     0, 4}, new long[]{5938,  1, 4}, new long[]{6038,  2, 4}, new long[]{4892,  3, 4}, new long[]{4907,  4, 4}, new long[]{5062,  5, 4}
                ),
                List.of(
                        "contento", "triste", "dolor", "cansado", "miedo", "cansado",
                        "enfadado", "asustado", "sorprendido", "aburrido", "asustado", "avergonzado",
                        "orgulloso", "celoso", "confundido", "sí", "no", "ayuda",
                        "quiero", "yo", "tú", "agua", "comida", "más",
                        "casa", "colegio", "parque", "mamá", "papá", "amigo"
                )
        );

        createBoardIfNotExists("Alimentos - Nivel 3",
                "Vocabulario completo de alimentos y bebidas.",
                5, 6, 3, "Alimentos",
                List.of(
                        new long[]{6456,  0, 0},
                        new long[]{2248,  1, 0},
                        new long[]{11461, 2, 0},
                        new long[]{2445,  3, 0},
                        new long[]{4389,  4, 0},
                        new long[]{5264,  5, 0},
                        new long[]{28339, 0, 1},
                        new long[]{2462,  1, 1},
                        new long[]{2530,  2, 1},
                        new long[]{6525,  3, 1},
                        new long[]{4385,  4, 1},
                        new long[]{4338,  5, 1},
                        new long[]{2573,  0, 2},
                        new long[]{2534,  1, 2},
                        new long[]{2316,  2, 2},
                        new long[]{2494,  3, 2},
                        new long[]{4387,  2, 2},
                        new long[]{4384,  3, 2},
                        new long[]{4903,  0, 3}, new long[]{4902,  1, 3}, new long[]{4901,  2, 3}, new long[]{4904,  3, 3}, new long[]{4905,  4, 3}, new long[]{4908,  5, 3},
                        new long[]{36518, 0, 4}, new long[]{6632,  1, 4}, new long[]{34027, 2, 4}, new long[]{26061, 3, 4}, new long[]{3220,  4, 4}, new long[]{11367, 5, 4}
                ),
                List.of(
                        "comer", "agua", "zumo", "leche", "zumo", "fruta",
                        "fruta", "manzana", "plátano", "galletas", "yogur", "huevo",
                        "sopa", "muslo", "carne", "pan", "manzana", "jamón",
                        "zanahoria", "patata", "naranja", "plátano", "pera", "uva",
                        "quiero", "yo", "sí", "no", "más", "ayuda"
                )
        );
    }


    // ** Extra boards ********************************************************

    private void createExtraBoards() {
        createBoardIfNotExists("Números - Nivel 2",
                "Tablero de números del 0 al 9 para nivel intermedio.",
                4, 3, 2, "Números",
                List.of(
                        new long[]{2626, 0, 0},
                        new long[]{2627, 1, 0},
                        new long[]{2628, 2, 0},
                        new long[]{2629, 0, 1},
                        new long[]{2630, 1, 1},
                        new long[]{2631, 2, 1},
                        new long[]{2632, 0, 2},
                        new long[]{2633, 1, 2},
                        new long[]{2634, 2, 2},
                        new long[]{2635, 0, 3}
                ),
                List.of("0","1","2","3","4","5","6","7","8","9")
        );

        createBoardIfNotExists("Números - Nivel 3",
                "Tablero de números del 0 al 9 para nivel avanzado.",
                4, 3, 3, "Números",
                List.of(
                        new long[]{2626, 0, 0},
                        new long[]{2627, 1, 0},
                        new long[]{2628, 2, 0},
                        new long[]{2629, 0, 1},
                        new long[]{2630, 1, 1},
                        new long[]{2631, 2, 1},
                        new long[]{2632, 0, 2},
                        new long[]{2633, 1, 2},
                        new long[]{2634, 2, 2},
                        new long[]{2635, 0, 3}
                ),
                List.of("0","1","2","3","4","5","6","7","8","9")
        );

        createBoardIfNotExists("Abecedario - Nivel 2",
                "Tablero del abecedario en español para nivel intermedio.",
                4, 7, 2, "Abecedario",
                List.of(
                        new long[]{3021, 0, 0},
                        new long[]{3023, 1, 0},
                        new long[]{3024, 2, 0},
                        new long[]{3025, 3, 0},
                        new long[]{3026, 4, 0},
                        new long[]{3027, 0, 1},
                        new long[]{3029, 1, 1},
                        new long[]{3030, 2, 1},
                        new long[]{3031, 3, 1},
                        new long[]{3032, 4, 1},
                        new long[]{3033, 0, 2},
                        new long[]{3035, 1, 2},
                        new long[]{3037, 2, 2},
                        new long[]{3038, 3, 2},
                        new long[]{3039, 4, 2},
                        new long[]{3040, 0, 3},
                        new long[]{3043, 1, 3},
                        new long[]{3044, 2, 3},
                        new long[]{3045, 3, 3},
                        new long[]{3046, 4, 3},
                        new long[]{3047, 0, 4},
                        new long[]{3412, 1, 4}
                ),
                List.of("a","b","c","d","e",
                        "f","h","i","j","k",
                        "l","m","o","p","q",
                        "r","u","v","w","x",
                        "y","ñ")
        );

        createBoardIfNotExists("Abecedario - Nivel 3",
                "Tablero del abecedario en español para nivel avanzado.",
                4, 7, 3, "Abecedario",
                List.of(
                        new long[]{3021, 0, 0},
                        new long[]{3023, 1, 0},
                        new long[]{3024, 2, 0},
                        new long[]{3025, 3, 0},
                        new long[]{3026, 4, 0},
                        new long[]{3027, 0, 1},
                        new long[]{3029, 1, 1},
                        new long[]{3030, 2, 1},
                        new long[]{3031, 3, 1},
                        new long[]{3032, 4, 1},
                        new long[]{3033, 0, 2},
                        new long[]{3035, 1, 2},
                        new long[]{3037, 2, 2},
                        new long[]{3038, 3, 2},
                        new long[]{3039, 4, 2},
                        new long[]{3040, 0, 3},
                        new long[]{3043, 1, 3},
                        new long[]{3044, 2, 3},
                        new long[]{3045, 3, 3},
                        new long[]{3046, 4, 3},
                        new long[]{3047, 0, 4},
                        new long[]{3412, 1, 4}
                ),
                List.of("a","b","c","d","e",
                        "f","h","i","j","k",
                        "l","m","o","p","q",
                        "r","u","v","w","x",
                        "y","ñ")
        );
    }

    // ** Helper ************************************************

    private void createBoardIfNotExists(
            String name, String description,
            int rows, int columns, int level, String category,
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
        board.setCategory(category);
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
                "https://static.arasaac.org/pictograms/%d/%d_300.png", id, id);
    }
}