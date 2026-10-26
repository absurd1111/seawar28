fun main() {
    val player = Player("Вы")
    val enemy = Player("Компьютер")

    printRules()

    println("=== РАССТАНОВКА ВАШИХ КОРАБЛЕЙ ===")
    placePlayerShips(player)

    println("\n=== КОМПЬЮТЕР РАССТАВЛЯЕТ КОРАБЛИ ===")
    placeEnemyShips(enemy)

    println("\n=== ИГРА НАЧАЛАСЬ ===")
    var playerTurn = true
    var totalMoves = 0

    while (player.hasShips() && enemy.hasShips()) {
        printBothFields(player, enemy)
        println("\nВаши корабли: ${player.countShips()} | Корабли врага: ${enemy.countShips()}") // Выводим остаток
        println()

        if (playerTurn) {
            print("Ваш ход. Введите координаты выстрела (row col) или 'quit': ")
            val input = readln().trim().split(" ")

            // ДЗ п.4: Выход из игры
            if (input[0].equals("quit", ignoreCase = true)) {
                println("Вы вышли из игры.")
                return
            }

            if (input.size != 2) {
                println("Ошибка: нужно два числа через пробел.")
                continue
            }
            val r = input[0].toIntOrNull()
            val c = input[1].toIntOrNull()
            if (r == null || c == null) {
                println("Ошибка: координаты должны быть числами.")
                continue
            }

            if (!enemy.isInside(r, c)) {
                println("Выстрел за границей поля! Попробуйте снова.")
                continue
            }
            val cell = enemy.field()[r][c]
            if (cell == CellState.HIT || cell == CellState.MISS) {
                println("Сюда уже стреляли! Попробуйте снова.")
                continue
            }

            totalMoves++
            val hit = enemy.shoot(r, c)
            if (hit) {
                println("Попадание!")
            } else {
                println("Промах.")
                playerTurn = false
            }
        } else {
            println("Ход компьютера...")
            var r: Int
            var c: Int
            do {
                r = Random.nextInt(0, 10)
                c = Random.nextInt(0, 10)
            } while (player.field()[r][c] == CellState.HIT || player.field()[r][c] == CellState.MISS)

            totalMoves++
            val hit = player.shoot(r, c)
            if (hit) {
                println("Компьютер попал в ($r, $c)!")
            } else {
                println("Компьютер промахнулся в ($r, $c).")
                playerTurn = true
            }
        }
        println()
    }

    println("Игра длилась $totalMoves ходов.")

    if (enemy.hasShips()) {
        println("Победил компьютер!")
    } else {
        println("Вы победили!")
    }
}