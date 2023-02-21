package com.example.safe_car_plate.model.repositories

import com.example.safe_car_plate.R

object ConsejosData {
    private val destNombres = arrayOf(
        "Consumo alcohol",
        "Cinturón",
        "Límites de velocidad",
        "Planifica tu viaje",
        "Estacionamiento",
        "Velocidad en curvas",
        "Distancia adecuada",
        "Cuidado con el clima",
        "Cambios de carril",
        "Salud física",
    )


    private val destDescripciones = arrayOf(
        "Aunque pueda parecer obvio, beber y conducir es muy peligroso y debe evitarse a toda costa. Asimismo, no conduzcas si has ingerido algún tipo de medicamento que pueda afectar tu juicio o a tus facultades físicas, como aquellos que causan somnolencia.\n" + "\n" + "Además de las preocupaciones por la seguridad, estar bajo la influencia del alcohol o drogas puede costarte tu trabajo y tu licencia de conducir.",
        "El uso de los cinturones de seguridad no solo es necesario e imperativo, sino que es la forma más prevenible de reducir las lesiones. Más del 40% de las muertes relacionadas con accidentes ocurren cada año por no usar el cinturón de seguridad.",
        "Los límites de velocidad estarán señalizados a donde quiera que vayas. Recuerda mantenerte dentro de lo establecido en cuanto a la velocidad y estarás más seguro en la carretera.",
        "Hay todo tipo de eventos inesperados que pueden ocurrir en el camino, pero tener un plan te mantendrá preparado ante cualquier imprevisto. Cuanto mejor tengas planeado las entregas que deberás realizar (horas de conducción, rutas, clima, etc.), más seguro será tu viaje. No olvides prestar atención a todo lo que sucede a tu alrededor.\n" + "\n" + "Siempre que sea posible, evita viajar a través de bastante tráfico y en horas de máxima afluencia. Cuanto más tráfico, mayores son las probabilidades de un accidente.",
        "Elija bien su lugar de estacionamiento. Aparque el auto lo más cerca posible de su destino, en un lugar visible y bien iluminado. Evite las plazas de estacionamiento situadas cerca de arbustos o en plantas vacías o poco utilizadas.",
        "Ten mucho cuidado al hacer giros en una carretera con curvas o en una rampa de salida. Para evitar un accidente, disminuye la velocidad y así poder adaptarte a los cambios de carril y a los otros vehículos.\n" + "\n" + "Ejerce siempre una precaución \"extra\" por la noche, especialmente en situaciones de maniobras cerradas.",
        "Asegúrate de mantener una amplia distancia entre tú y cualquier vehículo que vaya delante de ti. Adicionalmente, tener la cabeza con la mirada al frente y reducir las distracciones te ayudará a evitar interacciones cercanas.",
        "Ciertas partes del país son conocidas por tener un clima impredecible, pero trata de mantenerte al tanto de los cambios y evita las condiciones de conducción peligrosas siempre que sea posible.",
        "Elige un carril y quédate en él. Solo si es necesario cambiar, muévete con mucho cuidado, siendo consciente de tus puntos ciegos al revisar constantemente tus espejos. Las probabilidades de un accidente aumentan dramáticamente cada vez que un vehículo se mueve a otro carril.",
        "Seguir una dieta más saludable y tener el tiempo de descanso necesario te mantendrán más alerta en el camino. Asegúrate de dormir bien y detente si te sientes cansado."

    )

    private val destImagenes = intArrayOf(
        R.drawable.drinker,
        R.drawable.seat,
        R.drawable.speed,
        R.drawable.plan,
        R.drawable.parking,
        R.drawable.road,
        R.drawable.distance,
        R.drawable.weather,
        R.drawable.rebasar,
        R.drawable.driver
    )

    val listData: ArrayList<Consejos>
        get() {
            val list = arrayListOf<Consejos>()
            for (position in destNombres.indices) {
                val dest = Consejos()
                dest.nombre = destNombres[position]
                dest.descripcion = destDescripciones[position]
                dest.foto = destImagenes[position]
                list.add(dest)
            }
            return list
        }
}