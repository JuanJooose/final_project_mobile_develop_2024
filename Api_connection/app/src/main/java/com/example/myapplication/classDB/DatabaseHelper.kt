package com.example.myapplication.classDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.data.models.Product

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "ProductsDatabase.db"
        const val DATABASE_VERSION = 1

        // Tabla de Productos
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PRICE = "price"
        const val COLUMN_PRODUCT_CATEGORY_ID = "category_id"  // Renombrado para evitar conflicto
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_LOTE = "lote"

        // Tabla de Categorías
        const val TABLE_CATEGORIES = "categories"
        const val COLUMN_CATEGORY_ID = "category_id"  // ID de categoría en la tabla de categorías
        const val COLUMN_CATEGORY_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla de Categorías
        val createCategoryTableQuery = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL UNIQUE
            )
        """
        db.execSQL(createCategoryTableQuery)

        // Crear tabla de Productos con clave foránea para la categoría
        val createProductTableQuery = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_PRODUCT_CATEGORY_ID INTEGER,  -- Cambiado para evitar conflicto
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE TEXT,
                $COLUMN_LOTE INTEGER,
                FOREIGN KEY($COLUMN_PRODUCT_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
            )
        """
        db.execSQL(createProductTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    // Función para insertar una categoría en la tabla categories
    fun insertCategory(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, name)
        }
        val categoryId = db.insert(TABLE_CATEGORIES, null, values)
        db.close()
        return categoryId
    }

    // Método para obtener el ID de una categoría por nombre
    fun getCategoryIdByName(categoryName: String): Long? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_CATEGORY_ID FROM $TABLE_CATEGORIES WHERE $COLUMN_CATEGORY_NAME = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(categoryName))

        var categoryId: Long? = null
        if (cursor.moveToFirst()) {
            categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))
        }
        cursor.close()
        db.close()
        return categoryId
    }

    // Función para obtener el último valor de lote
    private fun getLastLote(): Int {
        val db = readableDatabase
        val query = "SELECT MAX($COLUMN_LOTE) FROM $TABLE_PRODUCTS"
        val cursor = db.rawQuery(query, null)

        var lastLote = 0
        if (cursor.moveToFirst()) {
            lastLote = cursor.getInt(0)  // Obtiene el valor máximo de lote
        }
        cursor.close()
        db.close()
        return lastLote
    }

    // Función para insertar un producto y asociarlo a una categoría específica
    fun insertProduct(product: Product, categoryId: Long) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, product.id)
            put(COLUMN_TITLE, product.title)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_PRODUCT_CATEGORY_ID, categoryId)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_IMAGE, product.image)

            // Asignación automática del lote
            put(COLUMN_LOTE, getLastLote() + 1)
        }
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    // Obtener productos por categoría
    fun getProductsByCategory(categoryName: String): List<Product> {
        val db = readableDatabase
        val products = mutableListOf<Product>()

        // Consulta de productos en la categoría dada
        val query = """
            SELECT * FROM $TABLE_PRODUCTS AS p
            INNER JOIN $TABLE_CATEGORIES AS c
            ON p.$COLUMN_PRODUCT_CATEGORY_ID = c.$COLUMN_CATEGORY_ID
            WHERE c.$COLUMN_CATEGORY_NAME = ?
        """

        val cursor: Cursor = db.rawQuery(query, arrayOf(categoryName))
        with(cursor) {
            while (moveToNext()) {
                val product = Product(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    title = getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                    price = getDouble(getColumnIndexOrThrow(COLUMN_PRICE)),
                    category = getString(getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)),
                    description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    image = getString(getColumnIndexOrThrow(COLUMN_IMAGE)),
                    lote = getInt(getColumnIndexOrThrow(COLUMN_LOTE))
                )
                products.add(product)
            }
        }
        cursor.close()
        db.close()
        return products
    }
}
