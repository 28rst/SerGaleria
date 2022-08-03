package com.example.rafae.myappv4;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * YEY!
 * Created by Ra on 19/02/2018.
 */


public class LigaBD extends SQLiteOpenHelper{

    private static final int versaoBD = 18;
    private static final String nomeBD = "prolserhum.db";

    //------------------------------Querys Criação de tabelas-----BEGIN-------------------------------//
    private static final String criarBDAnimal = "CREATE TABLE 'animal' ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`especie` TEXT, `nomeComum` TEXT NOT NULL, " +
            "`sexo` TEXT, `reino` TEXT NOT NULL DEFAULT 'Animalia', " +
            "`ordem` TEXT, `familia` TEXT, `classe` TEXT, `genero` TEXT, " +
            "`filo` TEXT, `descricao` TEXT);";

    private static final String criarBDFicheiroProva = "CREATE TABLE 'ficheiroProva' ( `id` INTEGER NOT NULL, " +
            "`caminho` TEXT NOT NULL, `observacao_id` INTEGER NOT NULL, " +
            "`observalcao_utilizador_id` INTEGER NOT NULL, " +
            "FOREIGN KEY(`observacao_id`) REFERENCES `observacao`(`id`) " +
            "ON UPDATE CASCADE ON DELETE CASCADE, " +
            "PRIMARY KEY(`id`) );";

    private static final String criarBDFotoAnimal = "CREATE TABLE `fotografiaAnimal` ( `idFoto` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`caminho` TEXT NOT NULL, `observacao_utilizador_id` INTEGER NOT NULL );";

    private static final String criarBDFotoObs = "CREATE TABLE 'fotografiaObservacao' ( `idFoto` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`caminho` TEXT NOT NULL, `observacao_id` INTEGER NOT NULL, " +
            "`observacao_utilizador_id` INTEGER NOT NULL, " +
            "FOREIGN KEY(`observacao_id`) REFERENCES `observacao`(`id`) " +
            "ON UPDATE CASCADE ON DELETE CASCADE );";

    private static final String criarBDObs = "CREATE TABLE `observacao` ( `id` INTEGER NOT NULL, `nome` TEXT NOT NULL, " +
            "`idAnimal` INTEGER NOT NULL, `descricao` TEXT, " +
            "`ficheiroProva` TEXT, `longitude` DOUBLE NOT NULL, " +
            "`latitude` DOUBLE NOT NULL, `utilizador_id` INTEGER NOT NULL, " +
            "PRIMARY KEY(`id`) );";

    private static final String criarBDObsHasAni = "CREATE TABLE 'observacao_has_animal' ( `observacao_id` INTEGER NOT NULL, " +
            "`observacao_utilizador_id` INTEGER NOT NULL, " +
            "`animal_id` INTEGER NOT NULL, " +
            "PRIMARY KEY(`observacao_id`,`observacao_utilizador_id`,`animal_id`), " +
            "FOREIGN KEY(`observacao_id`) REFERENCES `observacao`(`id`) " +
            "ON UPDATE CASCADE ON DELETE CASCADE );";

    private static final String criarBDUtilizadores = "CREATE TABLE 'utilizadores' (" +
            "'id' TEXT NOT NULL AUTOINCREMENT, 'nomeUtilizador' TEXT NOT NULL, " +
            "'password' TEXT NOT NULL, 'avatar' TEXT NOT NULL, 'email' TEXT NOT NULL, " +
            "'nivelAcessco' INT NOT NULL, 'nivelPremium' INT NOT NULL, 'imagemBackground' TEXT NOT NULL, " +
            "PRIMARY KEY('id'));";
    //------------------------------Querys Criação de tabelas---------END---------------------------//

    //------------------------------Querys apagar de tabelas----------BEGIN--------------------------//
    private static final String apagarBDAnimal = "DROP TABLE 'animal';";
    private static final String apagarBDFicheiroProva = "DROP TABLE 'ficheiroProva';";
    private static final String apagarBDFotoAnimal = "DROP TABLE 'fotografiaAnimal';";
    private static final String apagarBDFotoObs = "DROP TABLE 'fotografiaObservacao';";
    private static final String apagarBDObs = "DROP TABLE 'observacao';";
    private static final String apagarBDObsHasAni = "DROP TABLE 'observacao_has_animal';";
    private static final String apagarBDUtilizadores = "DROP TABLE 'utilizadores';";

    //------------------------------Querys apagar de tabelas----------END--------------------------//

    //------------------------------Querys inserir nas tabelas----------BEGIN--------------------------//
    /*
    private static final String inserirBDAnimal = "DROP TABLE 'animal';";
    private static final String inserirBDFicheiroProva = "DROP TABLE 'ficheiroProva';";
    private static final String inserirBDFotoAnimal = "DROP TABLE 'fotografiaAnimal';";
    private static final String inserirBDFotoObs = "DROP TABLE 'fotografiaObservacao';";
    private static final String inserirBDObs = "DROP TABLE 'observacao';";
    private static final String inserirBDObsHasAni = "DROP TABLE 'observacao_has_animal';";
    private static final String inserirBDUtilizadores = "DROP TABLE 'utilizadores';";
    */

    //------------------------------Querys inserir nas tabelas----------END--------------------------//


    public LigaBD(Context context) {
        super(context, nomeBD, null, versaoBD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(criarBDAnimal);
            db.execSQL(criarBDFicheiroProva);
            db.execSQL(criarBDFotoAnimal);
            db.execSQL(criarBDFotoObs);
            db.execSQL(criarBDObs);
            db.execSQL(criarBDObsHasAni);
            //db.execSQL(criarBDUtilizadores);

            Log.w("bd: ", "criada base de dados");

        } catch(SQLException e){
            Log.w("bd: ", "erro ao criar a base de dados");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("bd:","upgrade base de dados");
        db.execSQL(apagarBDAnimal);
        db.execSQL(apagarBDFicheiroProva);
        db.execSQL(apagarBDFotoAnimal);
        db.execSQL(apagarBDFotoObs);
        db.execSQL(apagarBDObs);
        db.execSQL(apagarBDObsHasAni);
        //db.execSQL(apagarBDUtilizadores);
        onCreate(db);
    }
}
