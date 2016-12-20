catwalkApp.service('$global.services', [
    'MartialartsSchool',
    'MartialartsStudent',
    'MartialartsKarateclass',
    'MartialartsAttendance',
    'MartialartsTuition',
    'MartialartsAddress',
    'MartialartsKarateclassexception',
    'UserManagerApplicationAuthority',
    'UserManagerApplicationPersistentToken',
    'UserManagerApplicationUser',
    'UserManagerApplicationUserAuthority',
    'UserManagerAppPassResetToken',
    'Account',
    'AppAPI',
function(
    MartialartsSchool,
    MartialartsStudent,
    MartialartsKarateclass,
    MartialartsAttendance,
    MartialartsTuition,
    MartialartsAddress,
    MartialartsKarateclassexception,
    UserManagerApplicationAuthority,
    UserManagerApplicationPersistentToken,
    UserManagerApplicationUser,
    UserManagerApplicationUserAuthority,
    UserManagerAppPassResetToken,
    Account,
    AppAPI
) {
            this.MartialartsSchool = MartialartsSchool;
            this.MartialartsStudent = MartialartsStudent;
            this.MartialartsKarateclass = MartialartsKarateclass;
            this.MartialartsAttendance = MartialartsAttendance;
            this.MartialartsTuition = MartialartsTuition;
            this.MartialartsAddress = MartialartsAddress;
            this.MartialartsKarateclassexception = MartialartsKarateclassexception;
            this.UserManagerApplicationAuthority = UserManagerApplicationAuthority;
            this.UserManagerApplicationPersistentToken = UserManagerApplicationPersistentToken;
            this.UserManagerApplicationUser = UserManagerApplicationUser;
            this.UserManagerApplicationUserAuthority = UserManagerApplicationUserAuthority;
            this.UserManagerAppPassResetToken = UserManagerAppPassResetToken;
            this.Account = Account;
        this.api = AppAPI;
}
]);
