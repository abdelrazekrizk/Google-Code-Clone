<?php

/**
 * UserEmployer filter form base class.
 *
 * @package    gtonline
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormFilterGeneratedTemplate.php 29570 2010-05-21 14:49:47Z Kris.Wallsmith $
 */
abstract class BaseUserEmployerFormFilter extends BaseFormFilterDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'userId'     => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('User'), 'add_empty' => true)),
      'employerId' => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('Employer'), 'add_empty' => true)),
      'jobTitle'   => new sfWidgetFormFilterInput(),
    ));

    $this->setValidators(array(
      'userId'     => new sfValidatorDoctrineChoice(array('required' => false, 'model' => $this->getRelatedModelName('User'), 'column' => 'id')),
      'employerId' => new sfValidatorDoctrineChoice(array('required' => false, 'model' => $this->getRelatedModelName('Employer'), 'column' => 'id')),
      'jobTitle'   => new sfValidatorPass(array('required' => false)),
    ));

    $this->widgetSchema->setNameFormat('user_employer_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'UserEmployer';
  }

  public function getFields()
  {
    return array(
      'id'         => 'Number',
      'userId'     => 'ForeignKey',
      'employerId' => 'ForeignKey',
      'jobTitle'   => 'Text',
    );
  }
}
