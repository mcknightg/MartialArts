'use strict';

//  Martialarts Karateclass Service
catwalkApp.factory('MartialartsKarateclass', ['MartialartsBaseService',function (MartialartsBaseService) {
    var entityUrl = MartialartsBaseService.getEntityUrl('karateclass');
    return MartialartsBaseService.getResource(entityUrl,{},{
        'columns':{method: 'POST', params:{},url:entityUrl + 'columns'},
        'api':{method: 'POST', params:{},url:entityUrl + 'api'},
        'schema':{method: 'POST', params:{},url:entityUrl + 'schema'}
    });
}
]);
