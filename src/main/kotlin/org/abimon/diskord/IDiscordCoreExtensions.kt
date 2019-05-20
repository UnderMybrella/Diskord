package org.abimon.diskord

import discordgamesdk.*

val IDiscordCore.applicationManager: IDiscordApplicationManager
    get() = get_application_manager.apply(this)
val IDiscordCore.userManager: IDiscordUserManager
    get() = get_user_manager.apply(this)
val IDiscordCore.imageManager: IDiscordImageManager
    get() = get_image_manager.apply(this)
val IDiscordCore.activityManager: IDiscordActivityManager
    get() = get_activity_manager.apply(this)