INSERT INTO employee(id, name, password, role)
values (generate_series(1, 20), %s, %s, (array['SCIENTIST', 'INSPECTOR']) [ floor(random() * 2 + 1)]);

PasswordEncoder bean = context.getBean(PasswordEncoder.class);
		String str = "INSERT INTO users(id,name,password,role) values (%s,%s,%s,%s);";
		Random random = new Random();
for(int i=0;i<20;i++) {
			String pass = "password" + i;
			String
role = random.nextInt() % 2 == 0? Role.INSPECTOR.name() : Role.SCIENTIST.name();
			String
encode = "'" + bean.encode(pass) + "'";
			String
user = "'" + "user" + i + "'";
			String
format = String.format(str, i, user, encode, "'" + role + "'");
			System.out.println
(format);
}


INSERT INTO reports
	(id,
	title,
	state,
	author_id,
	last_updated,
    file_name,
    file_uuid,
	comment)

	values

	(generate_series(1,1000000),
	substr(md5(random()::text), 1, 15),
	(array[
        'CREATED', 'APPROVED', 'REJECTED', 'SENT'
    ]) [floor(random() * 4 + 1)],
	random() * 19,
	NOW() + (random() * (NOW()+'-300 days' - NOW())) + '-100 days',
	substr(md5(random()::text), 1, 15),
	substr(md5(random()::text), 1, 15),
	substr(md5(random()::text), 1, 15)
	)