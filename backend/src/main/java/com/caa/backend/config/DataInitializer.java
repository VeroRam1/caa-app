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
                        new long[]{36518, 1, 0},
                        new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{5584,  4, 0},
                        new long[]{12264, 0, 1},
                        new long[]{7272,  1, 1},
                        new long[]{6456,  2, 1},
                        new long[]{6061,  3, 1},
                        new long[]{5526,  4, 1},
                        new long[]{12281, 0, 2},
                        new long[]{37160, 1, 2},
                        new long[]{6964,  2, 2},
                        new long[]{3082,  3, 2},
                        new long[]{3220,  4, 2},
                        new long[]{7186,  0, 3},
                        new long[]{7171,  1, 3},
                        new long[]{37827, 2, 3},
                        new long[]{13630, 3, 3},
                        new long[]{8129,  4, 3}
                ),
                List.of(
                        "Yo", "Quiero", "Mamá", "Papá", "sí",
                        "Mío", "tener hambre", "Comer", "Beber", "no",
                        "Tú", "necesitar", "casa", "colegio", "Más",
                        "nosotros", "necesito ayuda", "no entiendo", "estoy bien", "gracias"
                )
        );

        createBoardIfNotExists("Alimentos - Nivel 2",
                "Vocabulario ampliado de alimentos y bebidas.",
                3, 4, 2, "Alimentos",
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

        createBoardIfNotExists("Emociones - Nivel 2",
                "Vocabulario ampliado de emociones y estados de ánimo.",
                2, 4, 2, "Emociones",
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

        createBoardIfNotExists("Acciones - Nivel 2",
                "Verbos y acciones cotidianas básicas.",
                4, 5, 2, "Acciones",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{36518, 1, 0},
                        new long[]{6456,  2, 0},
                        new long[]{6061,  3, 0},
                        new long[]{6479,  4, 0},
                        new long[]{5584,  0, 1},
                        new long[]{6537,  1, 1},
                        new long[]{2315,  2, 1},
                        new long[]{6052,  3, 1},
                        new long[]{7141,  4, 1},
                        new long[]{5526,  0, 2},
                        new long[]{6495,  1, 2},
                        new long[]{6517,  2, 2},
                        new long[]{19524, 3, 2},
                        new long[]{35741, 4, 2},
                        new long[]{6611,  0, 3},
                        new long[]{6606,  1, 3},
                        new long[]{6044,  2, 3},
                        new long[]{6465,  3, 3},
                        new long[]{6607,  4, 3}
                ),
                List.of(
                        "yo", "quiero", "comer", "beber", "dormir",
                        "sí", "jugar", "cantar", "bailar", "leer",
                        "no", "estudiar", "hablar", "ayuda", "escuchar",
                        "sentar", "salir", "caminar", "correr", "saltar"
                )
        );
        createBoardIfNotExists("Personas - Nivel 2",
                "Vocabulario ampliado de personas y pronombres.",
                4, 4, 2, "Personas",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{6625,  1, 0},
                        new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{6481,  0, 1},
                        new long[]{7029,  1, 1},
                        new long[]{23718, 2, 1},
                        new long[]{23710, 3, 1},
                        new long[]{7186,  0, 2},
                        new long[]{7307,  1, 2},
                        new long[]{2457,  2, 2},
                        new long[]{2456,  3, 2},
                        new long[]{7033,  0, 3},
                        new long[]{38351, 1, 3},
                        new long[]{25790, 2, 3},
                        new long[]{2423,  3, 3}
                ),
                List.of(
                        "yo", "tú", "mamá", "papá",
                        "él", "ella", "abuelo", "abuela",
                        "nosotros", "vosotros", "maestro", "maestra",
                        "ellos", "familia", "amigo", "hermano"
                )
        );

        createBoardIfNotExists("Lugares - Nivel 2",
                "Vocabulario ampliado de lugares de interés.",
                3, 4, 2, "Lugares",
                List.of(
                        new long[]{6964,  0, 0},
                        new long[]{33068, 1, 0},
                        new long[]{15905, 2, 0},
                        new long[]{33074, 3, 0},
                        new long[]{2859,  0, 1},
                        new long[]{3082,  1, 1},
                        new long[]{15515, 2, 1},
                        new long[]{15551, 3, 1},
                        new long[]{3116,  0, 2},
                        new long[]{38870, 1, 2},
                        new long[]{3142,  2, 2},
                        new long[]{2826,  3, 2}
                ),
                List.of(
                        "casa", "dormitorio", "baño", "salón",
                        "parque", "colegio", "instituto", "centro comercial",
                        "hospital", "consulta", "piscina", "playa"
                )
        );

        createBoardIfNotExists("Descripciones - Nivel 2",
                "Tablero de adjetivos y descripciones para nivel intermedio.",
                4, 5, 2, "Descripciones",
                List.of(
                        new long[]{36480, 0, 0},
                        new long[]{4658, 1, 0},
                        new long[]{4693, 2, 0},
                        new long[]{4716, 3, 0},
                        new long[]{32392, 4, 0},
                        new long[]{36392, 0, 1},
                        new long[]{32440, 1, 1},
                        new long[]{32524, 2, 1},
                        new long[]{26002, 3, 1},
                        new long[]{26162, 4, 1},
                        new long[]{5397, 0, 2},
                        new long[]{25253, 1, 2},
                        new long[]{26172, 2, 2},
                        new long[]{26459, 3, 2},
                        new long[]{5306, 4, 2},
                        new long[]{5504, 0, 3},
                        new long[]{4767, 1, 3},
                        new long[]{4688, 2, 3},
                        new long[]{32388, 3, 3},
                        new long[]{4676, 4, 3}
                ),
                List.of(
                        "ser", "grande", "mediano", "pequeño", "igual",
                        "estar", "dulce", "salado", "corto", "largo",
                        "bien", "mojado", "limpio", "sucio", "rápido",
                        "mal", "vacío", "lleno", "roto", "lento"
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
                        new long[]{36518, 1, 0},
                        new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{5584,  4, 0},
                        new long[]{6522,  5, 0},
                        new long[]{12264, 0, 1},
                        new long[]{7272,  1, 1},
                        new long[]{6456,  2, 1},
                        new long[]{6061,  3, 1},
                        new long[]{5526,  4, 1},
                        new long[]{6028,  5, 1},
                        new long[]{12281, 0, 2},
                        new long[]{37160, 1, 2},
                        new long[]{6964,  2, 2},
                        new long[]{3082,  3, 2},
                        new long[]{3220,  4, 2},
                        new long[]{7061,  5, 2},
                        new long[]{7186,  0, 3},
                        new long[]{7171,  1, 3},
                        new long[]{37827, 2, 3},
                        new long[]{13630, 3, 3},
                        new long[]{8129,  4, 3},
                        new long[]{10273, 5, 3},
                        new long[]{5397,  0, 4},
                        new long[]{5504,  1, 4},
                        new long[]{22620, 2, 4},
                        new long[]{22621, 3, 4},
                        new long[]{36719, 4, 4},
                        new long[]{38907, 5, 4}
                ),
                List.of(
                        "Yo", "Quiero", "Mamá", "Papá", "sí", "hola",
                        "Mío", "tener hambre", "Comer", "Beber", "no", "adiós",
                        "Tú", "necesitar", "casa", "colegio", "Más", "¿como te llamas?",
                        "nosotros", "necesito ayuda", "no entiendo", "estoy bien", "gracias", "¿cómo estás?",
                        "bien", "mal", "qué", "cuándo", "por qué", "por favor"
                )
        );

        createBoardIfNotExists("Acciones - Nivel 3",
                "Vocabulario ampliado de verbos y acciones.",
                4, 5, 3, "Acciones",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{36518, 1, 0},
                        new long[]{6456,  2, 0},
                        new long[]{6061,  3, 0},
                        new long[]{6479,  4, 0},
                        new long[]{5584,  0, 1},
                        new long[]{6537,  1, 1},
                        new long[]{2315,  2, 1},
                        new long[]{6052,  3, 1},
                        new long[]{7141,  4, 1},
                        new long[]{5526,  0, 2},
                        new long[]{6495,  1, 2},
                        new long[]{6517,  2, 2},
                        new long[]{19524, 3, 2},
                        new long[]{35741, 4, 2},
                        new long[]{6611,  0, 3},
                        new long[]{6606,  1, 3},
                        new long[]{6044,  2, 3},
                        new long[]{6465,  3, 3},
                        new long[]{6607,  4, 3}
                ),
                List.of(
                        "yo", "quiero", "comer", "beber", "dormir",
                        "sí", "jugar", "cantar", "bailar", "leer",
                        "no", "estudiar", "hablar", "ayuda", "escuchar",
                        "sentar", "salir", "caminar", "correr", "saltar"
                )
        );

        createBoardIfNotExists("Emociones - Nivel 3",
                "Vocabulario completo de emociones y estados de ánimo.",
                2, 4, 3, "Emociones",
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

        createBoardIfNotExists("Alimentos - Nivel 3",
                "Vocabulario completo de alimentos y bebidas.",
                3, 4, 3, "Alimentos",
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

        createBoardIfNotExists("Personas - Nivel 3",
                "Vocabulario ampliado de personas y pronombres.",
                4, 4, 3, "Personas",
                List.of(
                        new long[]{6632,  0, 0},
                        new long[]{6625,  1, 0},
                        new long[]{2458,  2, 0},
                        new long[]{31146, 3, 0},
                        new long[]{6481,  0, 1},
                        new long[]{7029,  1, 1},
                        new long[]{23718, 2, 1},
                        new long[]{23710, 3, 1},
                        new long[]{7186,  0, 2},
                        new long[]{7307,  1, 2},
                        new long[]{2457,  2, 2},
                        new long[]{2456,  3, 2},
                        new long[]{7033,  0, 3},
                        new long[]{38351, 1, 3},
                        new long[]{25790, 2, 3},
                        new long[]{2423,  3, 3}
                ),
                List.of(
                        "yo", "tú", "mamá", "papá",
                        "él", "ella", "abuelo", "abuela",
                        "nosotros", "vosotros", "maestro", "maestra",
                        "ellos", "familia", "amigo", "hermano"
                )
        );

        createBoardIfNotExists("Lugares - Nivel 3",
                "Vocabulario ampliado de lugares de interés.",
                3, 4, 3, "Lugares",
                List.of(
                        new long[]{6964,  0, 0},
                        new long[]{33068, 1, 0},
                        new long[]{15905, 2, 0},
                        new long[]{33074, 3, 0},
                        new long[]{2859,  0, 1},
                        new long[]{3082,  1, 1},
                        new long[]{15515, 2, 1},
                        new long[]{15551, 3, 1},
                        new long[]{3116,  0, 2},
                        new long[]{38870, 1, 2},
                        new long[]{3142,  2, 2},
                        new long[]{2826,  3, 2}
                ),
                List.of(
                        "casa", "dormitorio", "baño", "salón",
                        "parque", "colegio", "instituto", "centro comercial",
                        "hospital", "consulta", "piscina", "playa"
                )
        );


        createBoardIfNotExists("Descripciones - Nivel 3",
                "Tablero de adjetivos y descripciones para nivel intermedio.",
                4, 5, 3, "Descripciones",
                List.of(
                        new long[]{36480, 0, 0},
                        new long[]{4658, 1, 0},
                        new long[]{4693, 2, 0},
                        new long[]{4716, 3, 0},
                        new long[]{32392, 4, 0},
                        new long[]{36392, 0, 1},
                        new long[]{32440, 1, 1},
                        new long[]{32524, 2, 1},
                        new long[]{26002, 3, 1},
                        new long[]{26162, 4, 1},
                        new long[]{5397, 0, 2},
                        new long[]{25253, 1, 2},
                        new long[]{26172, 2, 2},
                        new long[]{26459, 3, 2},
                        new long[]{5306, 4, 2},
                        new long[]{5504, 0, 3},
                        new long[]{4767, 1, 3},
                        new long[]{4688, 2, 3},
                        new long[]{32388, 3, 3},
                        new long[]{4676, 4, 3}
                ),
                List.of(
                        "ser", "grande", "mediano", "pequeño", "igual",
                        "estar", "dulce", "salado", "corto", "largo",
                        "bien", "mojado", "limpio", "sucio", "rápido",
                        "mal", "vacío", "lleno", "roto", "lento"
                )
        );

        createBoardIfNotExists("Tiempo - Nivel 3",
                "Vocabulario avanzado sobre conceptos temporales.",
                3, 5, 3, "Tiempo",
                List.of(
                        new long[]{38272, 0, 0},
                        new long[]{38279, 1, 0},
                        new long[]{38276, 2, 0},
                        new long[]{38278, 3, 0},
                        new long[]{38277, 4, 0},
                        new long[]{13028, 0, 1},
                        new long[]{13026, 1, 1},
                        new long[]{13080, 2, 1},
                        new long[]{7230,  3, 1},
                        new long[]{32396, 4, 1},
                        new long[]{26799, 0, 2},
                        new long[]{7268,  1, 2},
                        new long[]{26997, 2, 2}
                ),
                List.of(
                        "antes de ayer", "ayer", "hoy", "mañana", "pasado mañana",
                        "antes", "ahora", "después", "reloj", "fin de semana",
                        "día", "tarde", "noche"
                )
        );

        createBoardIfNotExists("Preguntas - Nivel 3",
                "Vocabulario avanzado de preguntas y partículas interrogativas.",
                2, 5, 3, "Preguntas",
                List.of(
                        new long[]{10276, 0, 0},
                        new long[]{32771, 1, 0},
                        new long[]{22620, 2, 0},
                        new long[]{22619, 3, 0},
                        new long[]{22621, 4, 0},
                        new long[]{10273, 0, 1},
                        new long[]{7061,  1, 1},
                        new long[]{32743, 2, 1},
                        new long[]{7764,  3, 1},
                        new long[]{36719, 4, 1}
                ),
                List.of(
                        "quién", "qué es?", "qué", "cómo", "cuándo",
                        "¿cómo estás?", "¿cómo te llamas?", "¿de quién es?", "dónde", "por qué"
                )
        );

        createBoardIfNotExists("Transporte - Nivel 3",
                "Vocabulario avanzado de medios de transporte.",
                3, 3, 3, "Transporte",
                List.of(
                        new long[]{2339,  0, 0},
                        new long[]{38090, 1, 0},
                        new long[]{35600, 2, 0},
                        new long[]{2277,  0, 1},
                        new long[]{7166,  1, 1},
                        new long[]{6932,  2, 1},
                        new long[]{2264,  0, 2},
                        new long[]{7126,  1, 2},
                        new long[]{2603,  2, 2}
                ),
                List.of(
                        "coche", "autobús", "furgoneta",
                        "bicicleta", "motocicleta", "barco",
                        "avión", "helicóptero", "tren"
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
                        new long[]{3027, 5, 0},
                        new long[]{3028, 6, 0},
                        new long[]{ 3029, 0, 1 },
                        new long[]{ 3030, 1, 1 },
                        new long[]{ 3031, 2, 1 },
                        new long[]{ 3032, 3, 1 },
                        new long[]{ 3033, 4, 1 },
                        new long[]{ 3035, 5, 1 },
                        new long[]{ 3036, 6, 1 },
                        new long[]{ 3412, 0, 2 },
                        new long[]{ 3037, 1, 2 },
                        new long[]{ 3038, 2, 2 },
                        new long[]{ 3039, 3, 2 },
                        new long[]{ 3040, 4, 2 },
                        new long[]{ 3041, 5, 2 },
                        new long[]{ 3042, 6, 2 },
                        new long[]{ 3043, 0, 3 },
                        new long[]{ 3044, 1, 3 },
                        new long[]{ 3045, 2, 3 },
                        new long[]{ 3046, 3, 3 },
                        new long[]{ 3047, 4, 3 },
                        new long[]{ 3048, 5, 3 }
                ),
                List.of("a", "b", "c", "d", "e", "f", "g",
                        "h", "i", "j", "k", "l", "m", "n",
                        "ñ", "o", "p", "q", "r", "s", "t",
                        "u", "v", "w", "x", "y", "z")
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
                        new long[]{3027, 5, 0},
                        new long[]{3028, 6, 0},
                        new long[]{ 3029, 0, 1 },
                        new long[]{ 3030, 1, 1 },
                        new long[]{ 3031, 2, 1 },
                        new long[]{ 3032, 3, 1 },
                        new long[]{ 3033, 4, 1 },
                        new long[]{ 3035, 5, 1 },
                        new long[]{ 3036, 6, 1 },
                        new long[]{ 3412, 0, 2 },
                        new long[]{ 3037, 1, 2 },
                        new long[]{ 3038, 2, 2 },
                        new long[]{ 3039, 3, 2 },
                        new long[]{ 3040, 4, 2 },
                        new long[]{ 3041, 5, 2 },
                        new long[]{ 3042, 6, 2 },
                        new long[]{ 3043, 0, 3 },
                        new long[]{ 3044, 1, 3 },
                        new long[]{ 3045, 2, 3 },
                        new long[]{ 3046, 3, 3 },
                        new long[]{ 3047, 4, 3 },
                        new long[]{ 3048, 5, 3 }
                ),
                List.of("a", "b", "c", "d", "e", "f", "g",
                        "h", "i", "j", "k", "l", "m", "n",
                        "ñ", "o", "p", "q", "r", "s", "t",
                        "u", "v", "w", "x", "y", "z")
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