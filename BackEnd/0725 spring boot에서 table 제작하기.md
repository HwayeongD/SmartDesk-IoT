## spring boot 테이블 만들기

- Datetime type 데이터 관리
  - https://devonce.tistory.com/51



- 언더바(_)가 있는 컬럼명 인식 불가 문제.

  - 언더바가 존재하면 Repository에서 findBy~ 매소드로 이름을 지을때 인식이 안됨.

  - `@Column annotation` 을 사용하면 해결 가능.

    - Entity

    ```java
    @Entity
    @Data
    @Table(name= "service_info")
    @Accessors(chain = true)
    public class ServiceInfoVO {
    
        @Id
        @Column(name = "n_service_index")
        int nServiceIndex;
    
        @Column(name = "service_name")
        String serviceName;
    
        @Column(name = "service_code")
        String serviceCode;
    
        @Column(name = "service_detail")
        String serviceDetail;
    
        String level;
    }
    ```

    - Repository

    ```java
    public interface ServiceInfoRepository extends CrudRepository<ServiceInfoVO, Long> {
    
        ServiceInfoVO findByServiceCode(String serviceCode);
    }
    ```



- Repository 에서 JpaRepository 인터페이스 이용하기.

  - Repository

    ```java
    @Repository
    public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
        // (select * from Employee where emp_id = ?)
        Optional<EmployeeEntity> findByEmpId(Long empId);
    }
    ```

    1. `save(S entity)`: 엔티티를 저장하거나 업데이트합니다.

    2. `findById(ID id)`: 주어진 ID에 해당하는 엔티티를 조회합니다.

    3. `findAll()`: 모든 엔티티를 조회합니다.

    4. `count()`: 엔티티의 총 개수를 반환합니다.

    5. `deleteById(ID id)`: 주어진 ID에 해당하는 엔티티를 삭제합니다.

       

  `findBy`, `getBy`, `readBy`, `queryBy`, `searchBy` 등의 키워드를 사용하여 메소드 이름을 정의하면 됩니다.



- Controller 에서 JSON 형식으로 데이터 반환하기

  - `@RestController` 어노테이션을 사용해야 한다.
  - `@Controller`와 `@ResponseBody`를 합친 것

  - 리턴 값이 HTTP 응답의 본문(body)으로 자동으로 변환

  ```java
  import org.springframework.web.bind.annotation.*;
  import org.springframework.http.*;
  
  @RestController
  public class YourController {
  
      @PostMapping("/login")
      public ResponseEntity<String> login(@ModelAttribute EmployeeDTO employeeDTO, HttpSession session) {
          EmployeeDTO loginResult = employeeService.login(employeeDTO);
          if (loginResult != null) {
              session.setAttribute("loginId", loginResult.getEmp_id());
              // 여기서 loginResult를 JSON으로 변환해서 반환
              return new ResponseEntity<>(toJson(loginResult), HttpStatus.OK);
          } else {
              return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
          }
      }
  
      // 임의로 JSON으로 변환하는 메서드를 작성하거나, JSON 변환 라이브러리를 사용 (예: Jackson)
      private String toJson(EmployeeDTO employeeDTO) {
          // JSON 변환 로직을 작성하여 employeeDTO를 JSON 문자열로 변환 후 반환
          // 예를 들어, Jackson ObjectMapper를 사용하는 경우:
          // ObjectMapper mapper = new ObjectMapper();
          // return mapper.writeValueAsString(employeeDTO);
  
          // 이 예제에서는 단순히 toString() 메서드를 사용해 문자열로 반환하도록 가정합니다.
          return employeeDTO.toString();
      }
  }
  ```



### HttpStatus 상태코드

1xx (Informational - 정보성 상태 코드):

- 100 Continue: 서버가 클라이언트의 요청을 받았으며, 계속해서 요청을 진행할 수 있음을 나타냅니다.

2xx (Successful - 성공 상태 코드):

- 200 OK: 요청이 성공적으로 처리되었음을 나타냅니다.
- 201 Created: 요청이 성공적으로 처리되었으며, 새로운 리소스가 생성되었음을 나타냅니다.
- 204 No Content: 요청은 성공적으로 처리되었지만, 응답 본문에 내용이 없음을 나타냅니다.

3xx (Redirection - 리다이렉션 상태 코드):

- 301 Moved Permanently: 요청한 리소스가 새로운 위치로 영구적으로 이동되었음을 나타냅니다.
- 302 Found: 요청한 리소스가 일시적으로 다른 위치에 있음을 나타냅니다.
- 304 Not Modified: 클라이언트의 캐시된 버전을 사용하여 요청한 리소스가 변경되지 않았음을 나타냅니다.

4xx (Client Error - 클라이언트 오류 상태 코드):

- 400 Bad Request: 클라이언트의 요청이 잘못되었음을 나타냅니다.
- 401 Unauthorized: 클라이언트가 인증되지 않았거나, 인증이 실패했음을 나타냅니다.
- 404 Not Found: 요청한 리소스를 찾을 수 없음을 나타냅니다.

5xx (Server Error - 서버 오류 상태 코드):

- 500 Internal Server Error: 서버에서 요청을 처리하는 중에 오류가 발생했음을 나타냅니다.
- 503 Service Unavailable: 서버가 일시적으로 요청을 처리할 수 없음을 나타냅니다.



















