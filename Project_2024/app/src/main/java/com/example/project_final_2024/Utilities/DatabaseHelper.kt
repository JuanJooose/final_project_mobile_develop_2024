package com.example.project_final_2024.Utilities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.project_final_2024.objets.Categoria
import com.example.project_final_2024.objets.Producto

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "InventarioDB"
        private const val DATABASE_VERSION = 1

        // Definición de tablas
        const val TABLE_PRODUCTOS = "Productos"
        const val TABLE_CATEGORIAS = "Categorias"

        // Columnas para la tabla Productos
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_PRECIO = "precio"
        const val COL_CATEGORIA_ID = "categoriaId"
        const val COL_IMAGEN = "imagen"
        const val COL_LOTE = "lote"

        // Columnas para la tabla Categorias
        const val COL_CATEGORIA_ID_ = "id"
        const val COL_CATEGORIA_NOMBRE = "nombre"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de categorías
        val createCategoriasTable = """
            CREATE TABLE $TABLE_CATEGORIAS (
                $COL_CATEGORIA_ID_ INTEGER PRIMARY KEY,
                $COL_CATEGORIA_NOMBRE TEXT NOT NULL
            );
        """.trimIndent()
        db?.execSQL(createCategoriasTable)

        // Crear tabla de productos
        val createProductosTable = """
    CREATE TABLE $TABLE_PRODUCTOS (
        $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COL_NOMBRE TEXT NOT NULL,
        $COL_PRECIO REAL NOT NULL,
        $COL_CATEGORIA_ID INTEGER,
        $COL_IMAGEN TEXT NOT NULL,    
        $COL_LOTE INTEGER,   
        FOREIGN KEY($COL_CATEGORIA_ID) REFERENCES $TABLE_CATEGORIAS($COL_CATEGORIA_ID_)
    );
""".trimIndent()

        db?.execSQL(createProductosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTOS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS")
        onCreate(db)
    }

    fun insertarProducto(id: String, nombre: String, precio: String, categoriaId: String, imagen: String, lote: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
//            put(COL_ID, id)
            put(COL_NOMBRE, nombre)
            put(COL_PRECIO, precio)
            put(COL_CATEGORIA_ID, categoriaId)
            put(COL_IMAGEN, imagen)
            put(COL_LOTE, lote)
        }
        val result = db.insert(TABLE_PRODUCTOS, null, values)
        return result != -1L
    }

    // Inserta una categoría si no existe
    fun insertarCategoria(id: Int, nombre: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_CATEGORIA_ID_, id)
            put(COL_CATEGORIA_NOMBRE, nombre)
        }
        db.insertWithOnConflict(TABLE_CATEGORIAS, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
    }

    // Verifica si una categoría existe en la base de datos
    fun existeCategoria(id: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CATEGORIAS WHERE $COL_CATEGORIA_ID_ = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        val exists = cursor.getInt(0) > 0
        cursor.close()
        db.close()
        return exists
    }

    // Obtiene el ID de la categoría según su nombre
    fun obtenerIdCategoriaPorNombre(categoriaNombre: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COL_CATEGORIA_ID_ FROM $TABLE_CATEGORIAS WHERE $COL_CATEGORIA_NOMBRE = ?", arrayOf(categoriaNombre))

        // Verificamos si la consulta obtuvo algún resultado
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("id")  // Busca el índice de la columna "id"

            // Si la columna existe, extraemos el valor
            if (columnIndex != -1) {
                val id = cursor.getInt(columnIndex)
                cursor.close()
                return id
            } else {
                Log.e("Database", "Columna 'id' no encontrada en la consulta.")
            }
        }
        cursor.close()
        return null  // Retorna null si no se encontró la categoría
    }

    @SuppressLint("Range")
    fun obtenerMaximoIdCategoria(): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT MAX($COL_CATEGORIA_ID_) AS maxId FROM $TABLE_CATEGORIAS", null)
        var maxId: Int? = null

        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(cursor.getColumnIndex("maxId"))
        }
        cursor.close()
        return maxId
    }


    @SuppressLint("Range")
    fun obtenerCategorias(): List<Categoria> {
        val db = this.readableDatabase
        val categorias = mutableListOf<Categoria>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORIAS", null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_CATEGORIA_ID_))
                val nombre = cursor.getString(cursor.getColumnIndex(COL_CATEGORIA_NOMBRE))
                categorias.add(Categoria(id, nombre))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return categorias
    }

    @SuppressLint("Range")
    fun obtenerProductos(): List<Producto> {
        val db = this.readableDatabase
        val productos = mutableListOf<Producto>()
        val cursor = db.query(
            TABLE_PRODUCTOS, // Tabla
            arrayOf(COL_ID, COL_NOMBRE, COL_PRECIO, COL_CATEGORIA_ID, COL_IMAGEN, COL_LOTE), // Columnas, ahora con imagen y lote
            null, // No hay cláusula WHERE
            null, // No hay argumentos
            null, // No agrupar
            null, // No ordenar
            null // No ordenar
        )

        // Si se obtienen resultados
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                val precio = cursor.getDouble(cursor.getColumnIndex(COL_PRECIO))
                val categoriaId = cursor.getInt(cursor.getColumnIndex(COL_CATEGORIA_ID))
                val imagen = cursor.getString(cursor.getColumnIndex(COL_IMAGEN))
                val lote = cursor.getInt(cursor.getColumnIndex(COL_LOTE))

                // Crear objeto Producto y agregarlo a la lista
                val producto = Producto(id, nombre, precio, categoriaId, imagen, lote)
                productos.add(producto)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return productos
    }

    fun actualizarStock(productId: Int, incrementoStock: Int) {
        val db = writableDatabase

        // Consultar el stock actual
        val cursor = db.rawQuery("SELECT $COL_LOTE FROM $TABLE_PRODUCTOS WHERE $COL_ID = ?", arrayOf(productId.toString()))
        if (cursor.moveToFirst()) {
            val stockActual = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOTE))
            cursor.close()

            // Sumar el incremento al stock actual
            val nuevoStock = stockActual + incrementoStock

            // Actualizar el stock en la base de datos
            val values = ContentValues().apply {
                put(COL_LOTE, nuevoStock)
            }
            db.update(TABLE_PRODUCTOS, values, "$COL_ID = ?", arrayOf(productId.toString()))
        } else {
            cursor.close()
            throw IllegalArgumentException("El producto con ID $productId no existe en la base de datos")
        }

        db.close()
    }

    fun facturarStock(productId: Int, cantidad: Int) {
        val db = writableDatabase

        // Consultar el stock actual
        val cursor = db.rawQuery("SELECT $COL_LOTE FROM $TABLE_PRODUCTOS WHERE $COL_ID = ?", arrayOf(productId.toString()))
        if (cursor.moveToFirst()) {
            val stockActual = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOTE))
            cursor.close()

            // Restar la cantidad al stock actual
            val nuevoStock = stockActual - cantidad

            // Verifica que el nuevo stock no sea negativo
            if (nuevoStock >= 0) {
                // Actualizamos el stock en la base de datos
                val values = ContentValues().apply {
                    put(COL_LOTE, nuevoStock)
                }
                db.update(TABLE_PRODUCTOS, values, "$COL_ID = ?", arrayOf(productId.toString()))
            } else {
                throw IllegalArgumentException("El stock es insuficiente para el producto con ID: $productId")
            }
        } else {
            cursor.close()
            throw IllegalArgumentException("El producto con ID $productId no existe en la base de datos")
        }

        db.close()
    }


}

