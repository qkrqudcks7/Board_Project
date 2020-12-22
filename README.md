# 최종 프로젝트 ( 게시판 만들기고 호스팅하기 )

***

## 1.  html / css/ js


>bootstrap를 사용하여 css를 담당하였습니다.
<pre>
<code>
link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"
</code>
</pre>
>jquery를 사용하여 부가 화면 이펙트를 구성했습니다.
<pre>
<code>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</code>
</pre>

>회원가입과 로그인, 게시글 작성 때 validation을 위해 js를 적용하였습니다.

  1. form에서 submit을 누르면 함수가 작동 됩니다.
  
  2. validation이 false로 취소되면 오류가 발생했다는 뜻입니다. 이벤트가 중지되어 form이 넘어가지 않습니다.
  
<pre>
<code>
input type="password" th:field="*{password}" placeholder="비밀번호를 입력하시오" required minlength="4" maxlength="50" class="form-control"
</code>
</pre>

  3.  위 코드에 required , minlength , maxlength 등이 validation의 동작 요소들입니다. 
      값이 없다면 required에서 false를 반환하고 min과 max도 그 값을 충족하지 못하면 false를 반환합니다.
      하지만 이 front쪽의 validation만 적용하면 다 뚫리기 때문에 back쪽으로 validation을 설정해야 합니다.
      
  4. front 쪽 전체 validation 코드입니다.
  

<pre>
<code>
<script type="application/javascript">
	(function () {
		'use strict';

		window.addEventListener('load', function () {
			var forms = document.getElementsByClassName('needs-validation');

			Array.prototype.filter.call(forms, function (form) {
				form.addEventListener('submit', function (event) {
					if (form.checkValidity() === false) {
						event.preventDefault();
						event.stopPropagation();
					}
					form.classList.add('was-validated')
				}, false)
			})
		}, false)
	}())
</script>
</code>
</pre>


부트스트랩 참조 : https://getbootstrap.com/docs/5.0/getting-started/introduction/


## 2. Spring boot , Postgresql (node 대체)

>node와 express 강의를 모두 듣고 따라 작성해보았습니다. 노드는 백지상태에서 모든걸 작성하는 것이라 자율성도 높고 제약되는 것이 없어 보였습니다.
하지만 너무 기초상태에서 게시판을 만들기란 역부족이었습니다. db연동까진 하지만, 그 후 db 객체를 가져와서 저장하고 , 수정하는 등 crud작업을 하기엔 
아직 너무 공부할 것이 많았습니다. 따라서 구현체를 spring boot로 대체 하였습니다.

>spring boot의 yml 파일에 db 저장주소와 id , password를 작성하였습니다.
git에 올릴 때 무조건 git.ignore에 yml 주소를 넣어야 하는 것을 배웠습니다. 안 그럼 큰일납니다.....
우선 로컬환경에 구동할 것이라, aws에 연결된 db가 아닌 제 로컬 환경 db 정보를 입력하였습니다.

<pre>
<code>
spring:
  datasource:
    url: jdbc:postgresql://---------------
    username: -------
    password: -------
    driver-class-name: org.postgresql.Driver
 </code>
 </pre>
 
 > spring security를 이용하여 로그인 인증객체를 사용하였습니다. 권한관리와 로그인 인증이 이루어집니다.
 코드를 보면 .anyRequest().authenticated()로 어느 페이지든 인증을 해야 접속할 수 있게 되어있습니다. 하지만
 ("/","/index","/account/login","/account/join")의 페이지들은 permit(허가) 시켰습니다.
 왜냐하면 메인 페이지와 로그인, 회원가입 하는 페이지는 들어갈 수 있어야하기 때문입니다.
 
 >다음 formLogin 방식으로 /account/login 주소로 들어오는 로그인 과정을 인증 과정으로 설정하였습니다.
 
 >마지막으로 로그아웃은 /account/logout 주소로 로그아웃 요청이 들어올 때  로그아웃이 성공하면 "/" 주소(메인페이지)로
 이동합니다. 그리고 Session을 종료합니다.
 <pre>
<code>
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/index","/account/login","/account/join").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/account/login").permitAll();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/account/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
    }
 </code>
 </pre>
 
 > crud 방식을 통해 입력된 정보를 저장하고 반환하고 수정 및 삭제 하는 과정으로 구현하였습니다.
 
 
## 3. aws
> 이때까지 개발환경에서 코드를 작성하고, 개발만 해왔습니다. 그러나 정작 호스팅은 한번도 해보지 않았습니다. 이번 기회에 aws 연동을 한번 도전해 보았습니다
전체적인 흐름은 ec2 생성 , pem파일을 이용해 ec2-user 환경으로 build하는 것입니다. 거기에 db를 이용하기 때문에 rds를 생성하여 ec2와 연동한는 작업도 추가로
진행하였습니다.

  1. ec2 생성
  
    구글링을 통해 ec2 인스턴스를 생성하는 과정을 배웠습니다. 아무것도 모르는 상태에서 시작하니, 정말 어려웠습니다.
    ec2 인스턴스 생성과정에 보안 규칙이 무엇인지 또한 탄력적 ip란 무엇인지 배웠습니다. 매번 연결할 때 마다 ip가 바뀌니, 고정된 ip를 할당해주는 탄력적 ip를 받아서
    ec2-user 환경에 접속할 수 있었습니다.
  2. rds 생성
  
    진심으로 이때 포기할뻔 했습니다. 정말 교수님의 도움이 컸습니다. 구현물에서 db를 사용하기 때문에 구글링하여 aws에서 지원하는 rds를 생성해야 되는 것을 알았습니다.
    따라서 rds를 postgresql로 생성했습니다. 제 로컬 postgres와 rds를 연동하기 위해서는 rds의 엔드포인트와 username , password가 있어야 합니다.
    여기서 username이 DB식별자 이름인줄 알고 정말 한 4시간동안 고생했습니다. ※ username과 DB식별자 이름은 서로 다릅니다. ※
    또한 rds의 보안 그룹 설정에서 교수님께 많은 도움을 받았습니다. 보안 그룹에 내 ip주소를 추가해야 함을 깨달았습니다.
    그렇게 rds와 제 postgres를 연동시킬 수 있었습니다. 로컬환경에서 yml을 rds주소 정보로 모두 바꿔 구동시켰고, 성공했을 때 정말 기뻤습니다. 하지만....
    
    ec2 환경으로 rds에 접근에 오류가 나는 것을 깨달았을 땐, 진짜 절망적이었습니다. 도대체 뭐가 잘못된걸까? 다 정상적으로 연결된 것 같은데....
    그래서 처음부터 다시 시작했습니다. rds에서 보안규칙을 설정하는 것 부터 시작했습니다. 제 ec2 인스턴스의 보안그룹에 해당되는 보안 규칙 따로 빼서 rds 전용 보안그룹을 생성하였습니다.
    그랬더니 ec2환경에서 rds에 접근할 수 있었습니다. 저녁도 안 먹고 새벽2시까지 몰두한 보람이 있었습니다.
    
    
  3. build
    
    git에 구현물을 push하고 그 주소 받아서, ec2환경에서 build를 하는 방법이 있습니다.
    따라서 git으로부터 주소를 받아와서, ec2 가상환경에서 git clone ~~~~~~~~~~~~주소~~~~~~~~~~~~ 를 통해 구현체를 받아왔습니다.
    그렇게 build를 하니 자꾸 오류가 나는 것이었습니다. Caused by: org.springframework.boot.autoconfigure.jdbc.DataSourceProperties$DataSourceBeanCreationException at DataSourceProperties
    구글링을 한 결과... git.ignore에 작성했던 yml주소가 문제였습니다. 어떻게 생각해보면 당연한 것이었습니다. git.ignore에 yml을 추가하니, 당연히 git에는 yml은 올라가지 않습니다.
    따라서 build를 할 때 yml없이 build를 하니 당연히 build에 오류가 나는 것이었습니다. 따라서 vim application.yml을 입력하여 복붙하여 ec2 환경에 업로드 하였다.
    드디어 build success라는 창이 떴을 때, 눈물 날뻔 했습니다. 그러나 하나의 큰 산이 더 남았다는 것을 몰랐습니다.....
    
    개발환경과 서버환경에서 구동되는 구현체가 많이 다른점이 있다는 것을 깨달았습니다. 특히 개발환경에서는 이상 없이 잘 되다가, 서버환경에서 구동하면 오류가 나는 점이 많았습니다.
    
    특히 주소를 입력할 때의 "/" 이 슬래쉬가 주요한 원인이었습니다. 절대경로와 상대경로의 이해 문제였습니다. 그렇다면 개발환경에선 왜 돌아갔던 거지? 라고 생각해보면 인텔리제이에서 절대경로를
    모두 자동으로 올바르게 수정해서 구동 됐던 것이고, 서버환경에는 그것을 자동으로 못하니깐 오류가 나는 것이었습니다. 올바르게 수정한 후 다시 성공적으로 실행했습니다.
    
  4. nohup
    
    ec2 가상환경을 종료하면 제 호스팅 주소도 종료됐습니다. 따라서 백그라운드 환경에서도 돌아갈 수 잇도록 하는 방법을 검색하였고 
    nohup로 돌리면 되는 것을 배웠습니다. jar파일을  실행하기 전 nohup를 입력하고 마지막에 &를 작성하면 가상환경을 종룧해도 백그라운드 환경에서 돌아갑니다.
    하지만 여기서 한 10번을 실패했습니다. 그 이유는 제가 실행만 했지 exit로 가상환경을 닫지 않았기 때문입니다. 꼭 nohup 실행 후 exit를 입력해야 합니다.
    
    
