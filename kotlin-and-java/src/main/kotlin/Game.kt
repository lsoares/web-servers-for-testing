interface ProfileApiClient {
    fun saveProfile(profile: Profile)
    fun getProfile(): Profile
}

data class Profile(val name: String)

class Game(private val profileApiClient: ProfileApiClient) {

    fun saveProfile(profile: Profile) {
        profileApiClient.saveProfile(profile)
    }

    fun getProfile() = profileApiClient.getProfile()
}

