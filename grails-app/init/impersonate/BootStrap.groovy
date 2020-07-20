package impersonate

import grails.gorm.transactions.Transactional

class BootStrap {

  def init = {
      addTestUser()
  }

  @Transactional
  void addTestUser() {
      def adminRole = new Role(authority: 'ROLE_ADMIN').save()
      def userRole = new Role(authority: 'ROLE_USER').save()
      def switchUserRole = new Role(authority: 'ROLE_SWITCH_USER').save()

      def testUser = new User(username: 'me', password: 'password').save()
      def otherUser = new User(username: 'other', password: 'password').save()

      UserRole.create testUser, adminRole
      UserRole.create testUser, switchUserRole

      UserRole.create otherUser, userRole

      UserRole.withSession {
          it.flush()
          it.clear()
      }

      assert User.count() == 2
      assert Role.count() == 3
      assert UserRole.count() == 3
  }

    def destroy = {
    }
}
