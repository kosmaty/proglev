(function () {
    "use strict";
    angular.module('progesteroneLevel')
        .factory('pregnancyService', function pregnancyService($http){
            var baseUrl = 'http://localhost:8080/api/pregnancies/';

            return {
                get: function(){
                    return $http.get(baseUrl)
                        .then(response => response.data);
                },
                find: function(id){
                    return $http.get(baseUrl + id)
                        .then(response => response.data);
                },
                create: function(pregnancy){
                    return $http.post(baseUrl, pregnancy);
                },
                update: function(pregnancy){
                    return $http.put(baseUrl + "/" + pregnancy.id, pregnancy);
                },
                destroy: function(id){
                    contacts.splice(id, 1);
                }
            };
        });
})();