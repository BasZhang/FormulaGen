package ${packageName};


/**
 * 游戏中用到的公式。
 *
 */
public enum ${className} {

	<#if (formulasList?size > 0)>
	<#list formulasList as f>
	/**
	 * ${f.name}
	 */
	${f.name} {
		/**
		 * 计算${f.name}
		 * <p>
		 * ${f.desc}
		 * @param a 参数
		 * @return 得数
		 */
		@Override
		public double calculate(double... a) {
			<#if f.formula?contains(";")>
			${f.formula}
			<#else>
			return ${f.formula};
			</#if>
		}
	<#if f_has_next>
	},
	<#else>
	};
	</#if>
	</#list>
	<#else>
	/**
	 * 未配置公式
	 */
	F_NON;
	</#if>

	/**
	 * 计算公式
	 * 
	 * @param a 参数
	 * @return 默认返回-1
	 */
	public double calculate(double... a) {
		return -1;
	}
}

