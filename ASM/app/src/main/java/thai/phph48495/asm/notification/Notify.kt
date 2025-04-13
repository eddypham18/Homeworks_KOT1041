package thai.phph48495.asm.notification

import java.util.Date
import java.util.UUID

data class Notify(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val message: String,
    val dateCreated: Date = Date(),
    val isRead: Boolean = false,
    val type: String = "order",
    val imageUrl: String? = null,
    val relatedId: String? = null // ID liên quan (như OrderID)
)

