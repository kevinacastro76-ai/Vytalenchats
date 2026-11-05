# VytalEnchants

Plugin para **Paper 1.20.4 / Java 17**. Los encantamientos personalizados se entregan como libros, se muestran en un menú y se aplican mediante yunque.

## Subir a GitHub
1. Crea un repositorio llamado `VytalEnchants`.
2. Sube el contenido de esta carpeta a la raíz del repositorio.
3. Abre **Actions** y espera el workflow `Build VytalEnchants`.
4. Descarga el artefacto `VytalEnchants-1.0.0`.
5. Coloca el JAR en `plugins/` de tu servidor Paper 1.20.4 y reinicia.

## Compilar localmente
Necesitas Java 17 y Gradle 8 o superior:
```bash
gradle clean build
```
El JAR estará en `build/libs/VytalEnchants-1.0.0.jar`.

## Comandos
- `/ve` o `/ve menu`: abre el menú de categorías.
- `/ve list`: muestra el catálogo y sus IDs.
- `/ve book <id> <nivel> [jugador]`: entrega un libro.
- `/ve reload`: recarga configuración.

Ejemplo: `/ve book Minero3x3 1 TuNombre`

## Nota técnica
Los 200 encantamientos están registrados en el catálogo y se pueden obtener como libros personalizados y aplicar en yunque. Esta versión incluye efectos funcionales de ejemplo para **Minero3x3**, **FiloMortal** y **RoboDeVida**; el resto queda preparado en el catálogo para implementar sus efectos individuales en futuras versiones. No se debe interpretar que los 200 efectos de gameplay ya están programados.
