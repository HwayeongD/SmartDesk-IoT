### 스프링부트 환경세팅

https://start.spring.io/

- Dependecies
  - Spring Web
  - Thymeleaf



## 회원가입, 로그인, 맴버 리스트 확인 기능 구현

GitHub\SSAFY\스프링 공부\member



#### **build.gradle.kts**

```java
	compileOnly("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
    
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.2")
	implementation("mysql:mysql-connector-java:8.0.20")

	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
```

- **Lombok**
  - 개발을 간편하게 도와주는 라이브러리.
  - @Getter / @Setter: 필드별로 getter와 setter 메서드를 자동으로 생성합니다.
  - @NoArgsConstructor: 파라미터가 없는 기본 생성자를 자동으로 생성합니다.
  - @RequiredArgsConstructor: 초기화되지 않은 final 필드들에 대한 생성자를 자동으로 생성합니다.
  - @ToString: 클래스의 toString 메서드를 자동으로 생성합니다.
- **JPA**
  
- 데이터베이스와 자바 객체 간의 매핑, 쿼리 생성 등의 기능을 제공하여 개발자가 DB에 접근하고 조작하는 작업을 간소화해준다.
  
- **mysql:mysql-connector-java**

  - MySQL DB와의 연결을 위해 사용되는 MySQL Connector/J 라이브러리 추가.

    



#### **application.yml**

```yaml
# database 연동 설정
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    #    각자 PC에 만들어놓은 Database이름을 써야 합니다.
    url: jdbc:mysql://127.0.0.1:3306/member?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    #    mysql에 생성한 사용자 계정 정보를 써야 합니다.
    username: root
    password: 111111 # 자신의 mysql 비밀번호
  thymeleaf:
    cache: false

  # spring data jpa 설정
  jpa:
    database-platform: org.hibernate.dialect.MySQL57Dialect
    open-in-view: false
    show-sql: true
    hibernate:
      ddl-auto: update
```



#### MemberEntity.java

```java
package com.example.member.entity;

import com.example.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "member_table") //database에 해당 이름의 테이블 생성
public class MemberEntity { //table 역할
    //jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(unique = true)	//PK 설정
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        return memberEntity;
    }

}
```

#### Entity

- 데이터베이스 테이블과 매핑되는 개체.
- 데이터베이스에서 데이터를 저장, 수정, 삭제할 수 있는 객체
- 주로 JPA(Java Persistence API)나 ORM 프레임워크를 사용하여 데이터베이스와 상호작용
- 데이터베이스와 상호작용하여 비지니스 로직을 수행할 수 있음.





#### MemberDTO.java

```java
package com.example.member.dto;

import com.example.member.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//lombok dependency추가
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO { //회원 정보를 필드로 정의
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    //lombok 어노테이션으로 getter,setter,생성자,toString 메서드 생략 가능

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());

        return memberDTO;
    }
}
```

#### DTO(Data Transfer Object)

- 데이터 전송 객체
- 데이터를 전송하기 위한 목적으로 사용.
- 일반적으로 속성과 그에 해당하는 getter 및 setter 매서드를 가지고 있음.
- 비지니스 로직을 포함하지 않고, 단순히 데이터를 보유하는 객체

#### 



#### MemberController.java

```java
package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor //MemberService에 대한 멤버를 사용 가능
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")    // name값을 requestparam에 담아온다
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);

        return "login";
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }


    @PostMapping("/member/login") // session : 로그인 유지
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html로 가져갈 데이터가 있다면 model 사용
        model.addAttribute("memberList", memberDTOList);
        return "list";

    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        // login 처럼 return 값에 따라 분류 할 수 있음
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/delete/{id}") // /member/{id}로 할 수 있도록 공부
    public String deleteById(@PathVariable Long id){
        memberService.deleteByid(id);

        return "redirect:/member/"; // list 로 쓰면 껍데기만 보여짐
    }
}
```

#### Controller

- 클라이언트의 요청을 처리하고, 요청에 따라 적절한 Service 매서드를 호출하여 비지니스 로직을 실행한다.
- 이때, 클라이언트가 보낸 데이터를 DTO를 사용하여 전달하고나, Service로부터 반환된 결과를 DTO를 사용하여 응답으로 반환.
- 클라이언트에서 새로운 사용자 정보를 등록 요청할 때, Controller는 해당 데이터를 UserDTO 객체로 변환하여 Service에 전달한다.





#### MemberService.java

```java
package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service //스프링이 관리해주는 객체 == 스프링 빈
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class MemberService {

    private final MemberRepository memberRepository; // 먼저 jpa, mysql dependency 추가

    public void save(MemberDTO memberDTO) {
        // repsitory의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        //Repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

    }

    public MemberDTO login(MemberDTO memberDTO){ //entity객체는 service에서만
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()){
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberEmail.get(); // Optional에서 꺼냄
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호 일치
                //entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                //비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없다
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        //Controller로 dto로 변환해서 줘야 함
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));

        }
        return memberDTOList;

    }

    public MemberDTO findById(Long id) {
        // 하나 조회할때 optional로 감싸줌
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get()); // optional을 벗겨내서 entity -> dto 변환
        }else {
            return null;
        }
    }

    public void deleteByid(Long id) {
        memberRepository.deleteById(id);
    }
}
```

#### Service

- 비지니스 로직을 처리하는 컴포넌트
- Controller로부터 전달받은 DTO를 Entity로 변환하거나, Entity를 DTO로 변환하여 데이터베이스와 상호작용한다.
- 데이터베이스에 저장하고 싶으면 전달받은 DTO를 Entity로 변환하여 데이터베이스에 저장한다.





#### MemberRepository.java

```java
package com.example.member.repository;

import com.example.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>
{
    // 이메일로 회원 정보 조회( select * from member_table where member_email=?)
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
```

#### Repository

- Repository는 데이터베이스와의 상호작용을 처리하는 인터페이스입니다. 데이터베이스에 접근하여 Entity를 생성, 수정, 삭제, 조회하는데 사용됩니다. 주로 Spring Data JPA를 사용하여 인터페이스만 정의하고, 구현은 자동으로 생성됩니다.













